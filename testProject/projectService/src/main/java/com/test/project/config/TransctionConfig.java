package com.test.project.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Aspect
@EnableTransactionManagement
@Configuration
public class TransctionConfig {

	@Autowired PlatformTransactionManager txManager;
	
	@Bean
	public TransactionInterceptor txAdvice(){
		
		TransactionInterceptor txAdvice = new TransactionInterceptor();

		List<RollbackRuleAttribute> rollbackRules = new ArrayList<RollbackRuleAttribute>();
		rollbackRules.add(new RollbackRuleAttribute(Exception.class));

		DefaultTransactionAttribute attribute = new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);

		String transactionAttributesDefinition = attribute.toString();

		Properties txAttributes = new Properties();
		txAttributes.setProperty("*", transactionAttributesDefinition);

		txAdvice.setTransactionAttributes(txAttributes);
		txAdvice.setTransactionManager(txManager);

		return txAdvice;
	}

	@Bean
	public Advisor txAdviceAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(* com.test.project.service.*.*(..))");
		return new DefaultPointcutAdvisor(pointcut, txAdvice());
	}

}
