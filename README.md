# snakeyaml-memshell
springboot跨线程注入拦截器型内存马
针对springboot yaml漏洞，加载jar包时会新建线程，故无法通过常规方法获取应用上下文，并且存在不同类加载器强制转换问题。
本工具通过Springboot内置TomcatClassLoader跨线程注入拦截器型内存马，注入成功后访问http://127.0.0.1/path?passer=whoami ；其中path可任意指定
