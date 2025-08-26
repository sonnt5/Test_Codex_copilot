package com.myrequestmanagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/request")
public class RequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        try (Connection conn = DatabaseUtil.getConnection()) {
            resp.getWriter().println("Connected to SQL Server: " + !conn.isClosed());
        } catch (SQLException e) {
            resp.getWriter().println("Database connection failed: " + e.getMessage());
        }
    }
}
