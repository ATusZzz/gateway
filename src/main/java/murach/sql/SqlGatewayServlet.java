package murach.sql;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/sqlGateway")
public class SqlGatewayServlet extends HttpServlet {

    private SqlGatewayDAO dao;

    @Override
    public void init() {
        dao = new SqlGatewayDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String sql = request.getParameter("sqlStatement");
        String result = dao.executeSql(sql);

        HttpSession session = request.getSession();
        session.setAttribute("sqlStatement", sql);
        session.setAttribute("sqlResult", result);

        getServletContext()
                .getRequestDispatcher("/index.jsp")
                .forward(request, response);
    }
}