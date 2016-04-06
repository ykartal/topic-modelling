package tr.com.ykartal.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import tr.com.ykartal.model.JarFile;

/**
 * Servlet implementation class JarFileList
 */
@WebServlet("/JarFileList")
public class JarFileListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public JarFileListServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JarFile jarFile = new JarFile();
        jarFile.setId(1L);
        jarFile.setFileName("TOJDE");
        List<JarFile> fileList = new ArrayList<JarFile>();
        fileList.add(jarFile);
        response.getOutputStream().println(MAPPER.writeValueAsString(fileList));
    }

}
