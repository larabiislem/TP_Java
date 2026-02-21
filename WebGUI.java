import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class WebGUI {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // ===== PAGE HTML =====
        server.createContext("/", exchange -> {
            String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Systeme Expert</title>
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body {
                        background: #1e1e2e;
                        color: #cdd6f4;
                        font-family: 'Segoe UI', sans-serif;
                        display: flex;
                        justify-content: center;
                        padding: 30px;
                    }
                    .container {
                        width: 700px;
                    }
                    h1 {
                        text-align: center;
                        color: #89b4fa;
                        font-size: 26px;
                        margin-bottom: 25px;
                    }
                    label {
                        display: block;
                        color: #9399b2;
                        font-weight: bold;
                        margin: 12px 0 6px;
                        font-size: 14px;
                    }
                    textarea {
                        width: 100%;
                        background: #313244;
                        color: #cdd6f4;
                        border: 2px solid #45475a;
                        border-radius: 10px;
                        padding: 12px;
                        font-family: 'Consolas', monospace;
                        font-size: 14px;
                        resize: vertical;
                    }
                    textarea:focus, input:focus {
                        outline: none;
                        border-color: #89b4fa;
                    }
                    input[type=text] {
                        width: 100%;
                        background: #313244;
                        color: #cdd6f4;
                        border: 2px solid #45475a;
                        border-radius: 10px;
                        padding: 10px 12px;
                        font-family: 'Consolas', monospace;
                        font-size: 14px;
                    }
                    .buttons {
                        display: flex;
                        gap: 12px;
                        justify-content: center;
                        margin: 20px 0;
                    }
                    button {
                        padding: 12px 30px;
                        border: 2px solid;
                        border-radius: 10px;
                        font-size: 15px;
                        font-weight: bold;
                        cursor: pointer;
                        transition: all 0.2s;
                    }
                    button:hover {
                        transform: translateY(-2px);
                        opacity: 0.9;
                    }
                    .btn-solve {
                        background: #a6e3a1;
                        color: #1e1e2e;
                        border-color: #a6e3a1;
                    }
                    .btn-example {
                        background: transparent;
                        color: #f9e2af;
                        border-color: #f9e2af;
                    }
                    .btn-clear {
                        background: transparent;
                        color: #f38ba8;
                        border-color: #f38ba8;
                    }
                    #result {
                        background: #181825;
                        color: #a6e3a1;
                        border: 2px solid #45475a;
                        border-radius: 10px;
                        padding: 15px;
                        font-family: 'Consolas', monospace;
                        font-size: 13px;
                        white-space: pre;
                        min-height: 200px;
                        max-height: 400px;
                        overflow-y: auto;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>🧠 Systeme Expert — Chainage Avant</h1>

                    <label>📜 Regles (une par ligne : A,B -> F)</label>
                    <textarea id="rules" rows="9" placeholder="A,B -> F"></textarea>

                    <label>📦 Faits initiaux (separes par des virgules)</label>
                    <input type="text" id="facts" placeholder="D,O,G">

                    <label>🎯 Objectif</label>
                    <input type="text" id="goal" placeholder="I">

                    <div class="buttons">
                        <button class="btn-example" onclick="loadExample()">📋 Exemple</button>
                        <button class="btn-solve" onclick="solve()">▶ Resoudre</button>
                        <button class="btn-clear" onclick="clearAll()">🗑 Effacer</button>
                    </div>

                    <label>📊 Resultats</label>
                    <div id="result">En attente...</div>
                </div>

                <script>
                    function loadExample() {
                        document.getElementById('rules').value =
                            "A,B -> F\\nF,H -> I\\nD,H,G -> A\\nO,G -> H\\nE,H -> B\\nG,A -> B\\nG,H -> P\\nG,H -> O\\nD,O,G -> J";
                        document.getElementById('facts').value = "D,O,G";
                        document.getElementById('goal').value = "I";
                        document.getElementById('result').textContent = "En attente...";
                    }

                    function clearAll() {
                        document.getElementById('rules').value = "";
                        document.getElementById('facts').value = "";
                        document.getElementById('goal').value = "";
                        document.getElementById('result').textContent = "En attente...";
                    }

                    async function solve() {
                        let rules = document.getElementById('rules').value;
                        let facts = document.getElementById('facts').value;
                        let goal  = document.getElementById('goal').value;

                        let params = "rules=" + encodeURIComponent(rules)
                                   + "&facts=" + encodeURIComponent(facts)
                                   + "&goal=" + encodeURIComponent(goal);

                        let response = await fetch("/solve", {
                            method: "POST",
                            headers: {"Content-Type": "application/x-www-form-urlencoded"},
                            body: params
                        });

                        let text = await response.text();
                        document.getElementById('result').textContent = text;
                    }
                </script>
            </body>
            </html>
            """;

            byte[] bytes = html.getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.getResponseBody().close();
        });

        // ===== API RÉSOLUTION =====
        server.createContext("/solve", exchange -> {
            // Lire les données POST
            String body = new String(exchange.getRequestBody().readAllBytes());
            String rules = "", facts = "", goal = "";

            for (String param : body.split("&")) {
                String[] kv = param.split("=", 2);
                String key = kv[0];
                String val = java.net.URLDecoder.decode(kv[1], "UTF-8");
                if (key.equals("rules")) rules = val;
                if (key.equals("facts")) facts = val;
                if (key.equals("goal"))  goal = val;
            }

            // Parser les règles
            String[] lines = rules.trim().split("\n");
            Rule[] bdr = new Rule[lines.length];
            for (int i = 0; i < lines.length; i++) {
                String[] parts = lines[i].trim().split("->");
                ArrayList<String> p = new ArrayList<>();
                for (String s : parts[0].trim().split(",")) p.add(s.trim());
                ArrayList<String> c = new ArrayList<>();
                for (String s : parts[1].trim().split(",")) c.add(s.trim());
                bdr[i] = new Rule(i, p, c);
            }

            // Parser les faits
            ArrayList<String> bdp = new ArrayList<>();
            for (String s : facts.trim().split(",")) {
                String f = s.trim();
                if (!f.isEmpty()) bdp.add(f);
            }

            // Lancer le moteur
            ArrayList<String> log = Engine.forward(bdr, bdp, goal.trim());

            // Construire la réponse
            StringBuilder sb = new StringBuilder();
            for (String l : log) sb.append(l).append("\n");

            byte[] bytes = sb.toString().getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.getResponseBody().close();
        });

        server.start();
        System.out.println("==========================================");
        System.out.println("  Serveur lance sur le port 8080");
        System.out.println("  Ouvre le lien dans ton navigateur !");
        System.out.println("==========================================");
    }
}