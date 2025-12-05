package com.yourorg.yourapp.config;

import com.yourorg.yourapp.usecase.annotation.UseCaseTransactional;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

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
        return txTemplate.execute(status -> {
            try {
                return pjp.proceed();
            } catch (RuntimeException | Error e) {
                throw e;
            } catch (Throwable t) {
                throw new RuntimeException(t);
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
}

