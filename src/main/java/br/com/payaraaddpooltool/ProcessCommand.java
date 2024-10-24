/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.payaraaddpooltool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author renanrodrigues
 */
public class ProcessCommand {

    // Mapa para armazenar os códigos de saída e suas descrições
    private static final Map<Integer, String> errorDescriptions = new HashMap<>();

    static {
        errorDescriptions.put(127, "Comando não encontrado");
        errorDescriptions.put(1, "Erro genérico");
        errorDescriptions.put(2, "Erro de uso do shell");
        errorDescriptions.put(126, "Comando invocado não executável");
        errorDescriptions.put(128, "Erro de saída com status incorreto");
        // Adicione outros códigos de erro comuns aqui
    }

    // Método para executar um comando no terminal e retornar a saída como String
    public static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", command);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Esperar o processo terminar e pegar o código de saída
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                // Buscar a descrição do erro no Map ou usar descrição genérica
                String errorDescription = errorDescriptions.getOrDefault(exitCode, "Erro desconhecido com código " + exitCode);
                output.append("Erro: ").append(errorDescription).append("\ncomando utilizado:\n").append(command);
            }

        } catch (Exception e) {
            return "Erro ao executar comando: " + e.getMessage();
        }

        return output.toString();
    }

    // Método para executar um comando sem capturar a saída
    public static void executeCommandWithoutOutput(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("Se perdeu bash", "-c", command);

            Process process = processBuilder.start();

            // Apenas espera o processo terminar
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                String errorDescription = errorDescriptions.getOrDefault(exitCode, "Erro desconhecido com código " + exitCode);
                System.out.println("Erro: " + errorDescription);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
