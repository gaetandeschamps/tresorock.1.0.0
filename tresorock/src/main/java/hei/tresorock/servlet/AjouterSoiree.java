package hei.tresorock.servlet;

import hei.tresorock.entities.Soiree;
import hei.tresorock.managers.ListeSoiree;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/session/addSoiree")
public class AjouterSoiree extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getSession().getAttribute("adminConnecte").equals("login")) {
            ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(req.getServletContext());
            templateResolver.setPrefix("/session/");
            templateResolver.setSuffix(".html");

            WebContext context = new WebContext(req, resp, req.getServletContext());

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            templateEngine.process("admin", context, resp.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // GET PARAMETERS
        String dateSoireeString = req.getParameter("dateSoiree");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateSoiree = LocalDate.parse(dateSoireeString, dateFormat);
        Double recetteCaisse = Double.parseDouble(req.getParameter("recetteCaisse"));
        String theme = req.getParameter("themeSoiree");

        // CREATE Soiree
        Soiree nouvelleSoiree = new Soiree(null,dateSoiree, recetteCaisse, theme);
        Soiree createdSoiree = ListeSoiree.getInstance().addSoiree(nouvelleSoiree);

        // REDIRECT TO Admin Page
        resp.sendRedirect(String.format("admin", createdSoiree.getId()));
    }
}