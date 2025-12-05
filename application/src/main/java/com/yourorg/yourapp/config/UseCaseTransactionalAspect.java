package com.yourorg.yourapp.config;

import com.yourorg.yourapp.usecase.annotation.UseCaseTransactional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
public class UseCaseTransactionalAspect {

    private final TransactionTemplate txTemplate;

    public UseCaseTransactionalAspect(PlatformTransactionManager txManager) {
        this.txTemplate = new TransactionTemplate(txManager);
    }

    @Around("@within(com.yourorg.yourapp.usecase.annotation.UseCaseTransactional) || @annotation(com.yourorg.yourapp.usecase.annotation.UseCaseTransactional)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        UseCaseTransactional ann = resolveAnnotation(pjp);
        if (ann == null) {
            return pjp.proceed();
        }
        txTemplate.setReadOnly(ann.readOnly());
        txTemplate.setPropagationBehavior(ann.requiresNew()
            ? TransactionDefinition.PROPAGATION_REQUIRES_NEW
            : TransactionDefinition.PROPAGATION_REQUIRED);
        if (ann.timeoutSeconds() >= 0) {
            txTemplate.setTimeout(ann.timeoutSeconds());
        }
        txTemplate.setName(buildTxName(pjp));

        // If a transaction already exists and we are not forcing REQUIRES_NEW, reuse it.
        if (!ann.requiresNew() && TransactionSynchronizationManager.isActualTransactionActive()) {
            return pjp.proceed();
        }

        return txTemplate.execute(status -> {
            try {
                return pjp.proceed();
            } catch (RuntimeException | Error e) {
                if (shouldRollbackFor(e, ann)) {
                    throw e;
                }
                // Swallow to commit (no-rollback scenario)
                return null;
            } catch (Throwable t) {
                if (shouldRollbackFor(t, ann)) {
                    throw new RuntimeException(t);
                }
                // Swallow to commit (no-rollback scenario)
                return null;
            }
        });
    }

    private UseCaseTransactional resolveAnnotation(ProceedingJoinPoint pjp) {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        UseCaseTransactional ann = AnnotatedElementUtils.findMergedAnnotation(sig.getMethod(), UseCaseTransactional.class);
        if (ann == null) {
            ann = AnnotatedElementUtils.findMergedAnnotation(sig.getDeclaringType(), UseCaseTransactional.class);
        }
        return ann;
    }

    private String buildTxName(ProceedingJoinPoint pjp) {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        return sig.getDeclaringType().getSimpleName() + "." + sig.getMethod().getName();
    }

    private boolean shouldRollbackFor(Throwable t, UseCaseTransactional ann) {
        for (Class<? extends Throwable> ex : ann.noRollbackFor()) {
            if (ex.isInstance(t)) {
                return false;
            }
        }
        if (ann.rollbackFor().length == 0) {
            return true;
        }
        for (Class<? extends Throwable> ex : ann.rollbackFor()) {
            if (ex.isInstance(t)) {
                return true;
            }
        }
        return false;
    }
}

