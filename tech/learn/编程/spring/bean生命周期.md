# 什么是循环依赖
引用相互依赖
## spring怎么解决循环依赖
1 通过setter注入不报循环依赖错误  
2 通过constructor注入报循环依赖错误

## spring的生命周期
```java
AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
InstanceA a = ctx.getBean(InstanceA.class);
ctx.close
```
通过上述代码，分析流程
1 org.springframework.context.support.AbstractApplicationContext#getBean(java.lang.Class<T>)
2 org.springframework.beans.factory.support.AbstractBeanFactory#doGetBean
    2.1 解析别名 org.springframework.beans.factory.support.AbstractBeanFactory#transformedBeanName
    2.2 先去缓存池去拿去 org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#getSingleton(java.lang.String)
    2.2 控制单例产生的 当前对象是否已经创建或者正在创建org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#isSingletonCurrentlyInCreation
singletonObjects
earlySingletonObjects
singletonFactories

容器创建的时候，单例缓存池就有了，研究一下bean是如何创建的
org.springframework.context.support.AbstractApplicationContext#refresh
org.springframework.context.support.AbstractApplicationContext#finishBeanFactoryInitialization
AbstractApplicationContext的refresh方法就是容器初始化的核心方法 具体是finishBeanFactoryInitialization方法调用


org.springframework.beans.factory.config.AbstractFactoryBean的dogetBean方法注解
```java
protected <T> T doGetBean(
			final String name, final Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
			throws BeansException {
        //别名解析           
		final String beanName = transformedBeanName(name);
		Object bean;
        //先去单例缓存池里去找，是否已经存在
		// Eagerly check singleton cache for manually registered singletons.
		Object sharedInstance = getSingleton(beanName);
		if (sharedInstance != null && args == null) {
			if (logger.isDebugEnabled()) {
                //判断当前bean是否还正在创建，如果还在创建，记录日志说可能发生循环依赖，否则返回缓存中的单例对象
				if (isSingletonCurrentlyInCreation(beanName)) {
					logger.debug("Returning eagerly cached instance of singleton bean '" + beanName +
							"' that is not fully initialized yet - a consequence of a circular reference");
				}
				else {
					logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
				}
			}
			bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
		}
        //在单例缓存池中没有拿到
		else {
			// Fail if we're already creating this bean instance:
			// We're assumably within a circular reference.
            //如果是原型对象，并且正在创建，抛出异常
			if (isPrototypeCurrentlyInCreation(beanName)) {
				throw new BeanCurrentlyInCreationException(beanName);
			}

			// Check if bean definition exists in this factory.
			BeanFactory parentBeanFactory = getParentBeanFactory();
			if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
				// Not found -> check parent.
				String nameToLookup = originalBeanName(name);
				if (args != null) {
					// Delegation to parent with explicit args.
					return (T) parentBeanFactory.getBean(nameToLookup, args);
				}
				else {
					// No args -> delegate to standard getBean method.
					return parentBeanFactory.getBean(nameToLookup, requiredType);
				}
			}

			if (!typeCheckOnly) {
				markBeanAsCreated(beanName);
			}

			try {
                //合并bean定义 原因 一个bean继承了另一个abstract=true的bean，就是把父类的熟悉合并到子类里边来 ，抽象的bean是不能被实例化的
				final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
                //检查我们合并过后的bean定义，如果是抽象的就抛出异常，表示bean不能被实例化
                //
                //bean定义就像是一张图纸，有了这个图纸才能建房子
				checkMergedBeanDefinition(mbd, beanName, args);

				// Guarantee initialization of beans that the current bean depends on.
                //检查依赖，有依赖的话继续注册依赖对象
				String[] dependsOn = mbd.getDependsOn(); 
				if (dependsOn != null) {
					for (String dep : dependsOn) {
						if (isDependent(beanName, dep)) {
							throw new BeanCreationException(mbd.getResourceDescription(), beanName,
									"Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
						}
                        //注册依赖的bean对象
						registerDependentBean(dep, beanName);
                        //实例化依赖对象
						getBean(dep);
					} 
				}

				// Create bean instance.
                //创建bean了
				if (mbd.isSingleton()) {
					sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
						@Override
						public Object getObject() throws BeansException {
							try {
								return createBean(beanName, mbd, args);
							}
							catch (BeansException ex) {
								// Explicitly remove instance from singleton cache: It might have been put there
								// eagerly by the creation process, to allow for circular reference resolution.
								// Also remove any beans that received a temporary reference to the bean.
								destroySingleton(beanName);
								throw ex;
							}
						}
					});
					bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
				}

				else if (mbd.isPrototype()) {
					// It's a prototype -> create a new instance.
					Object prototypeInstance = null;
					try {
						beforePrototypeCreation(beanName);
						prototypeInstance = createBean(beanName, mbd, args);
					}
					finally {
						afterPrototypeCreation(beanName);
					}
					bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
				}

				else {
					String scopeName = mbd.getScope();
					final Scope scope = this.scopes.get(scopeName);
					if (scope == null) {
						throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
					}
					try {
						Object scopedInstance = scope.get(beanName, new ObjectFactory<Object>() {
							@Override
							public Object getObject() throws BeansException {
								beforePrototypeCreation(beanName);
								try {
									return createBean(beanName, mbd, args);
								}
								finally {
									afterPrototypeCreation(beanName);
								}
							}
						});
						bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
					}
					catch (IllegalStateException ex) {
						throw new BeanCreationException(beanName,
								"Scope '" + scopeName + "' is not active for the current thread; consider " +
								"defining a scoped proxy for this bean if you intend to refer to it from a singleton",
								ex);
					}
				}
			}
			catch (BeansException ex) {
				cleanupAfterBeanCreationFailure(beanName);
				throw ex;
			}
		}

		// Check if required type matches the type of the actual bean instance.
		if (requiredType != null && bean != null && !requiredType.isInstance(bean)) {
			try {
				return getTypeConverter().convertIfNecessary(bean, requiredType);
			}
			catch (TypeMismatchException ex) {
				if (logger.isDebugEnabled()) {
					logger.debug("Failed to convert bean '" + name + "' to required type '" +
							ClassUtils.getQualifiedName(requiredType) + "'", ex);
				}
				throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
			}
		}
		return (T) bean;
	}
```