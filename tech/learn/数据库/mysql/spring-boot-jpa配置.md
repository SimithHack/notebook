```java
@Configuration
@EnableJpaRepositories(basePackages="com.test.lowvoltagevisualizationplatform.dao")
@EnableTransactionManagement
public class JpaConfig {

    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.getJpaPropertyMap();
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.test.lowvoltagevisualizationplatform.entity");
        factory.setDataSource(dataSource);
        factory.setJpaDialect(vendorAdapter.getJpaDialect());
        return factory;
    }

    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
```