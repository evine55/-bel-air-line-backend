package com.belairline.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Bel-Air Line</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background: #f2f5fa;
                        text-align: center;
                        padding: 80px 20px;
                    }
                    .container {
                        background: white;
                        padding: 40px;
                        border-radius: 12px;
                        max-width: 650px;
                        margin: auto;
                        box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                    }
                    h1 { color: #12345a; }
                    p { color: #555; }
                    a {
                        display: inline-block;
                        background: #12345a;
                        color: white;
                        padding: 12px 22px;
                        text-decoration: none;
                        border-radius: 6px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>WELCOME TO BEL-AIR LINE</h1>
                    <h2>Airline Management System</h2>
                    <p>Your backend application is running successfully.</p>
                    <p>Status: Online</p>
                    <a href="/api/dashboard/summary">View Dashboard API</a>
                </div>
            </body>
            </html>
            """;
    }
}
