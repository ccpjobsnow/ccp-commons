package com.ccp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class ExecutaScripts {
	public static void main(String[] args) throws Exception {

		String mensagemDeCommit = JOptionPane.showInputDialog("Digite sua mensagem de commit.");
		String path = new File("").getAbsolutePath();
		File parentFile = new File(path).getParentFile();
		String name = parentFile.getName();
		File[] listFiles = parentFile.listFiles();
		for (File file : listFiles) {
			String folderName = file.getName();
			boolean pastaNadaAVer = folderName.startsWith(name) == false;
			if (pastaNadaAVer) {
				continue;
			}
			boolean naoEhPasta = file.isDirectory() == false;
			if (naoEhPasta) {
				continue;
			}
			String pathDoGit = "C:\\Program Files\\Git\\cmd\\git";
			executarComando(pathDoGit
					+ " add .", file);
			String command = pathDoGit + " commit -m \""
					+ mensagemDeCommit
					+ "\"";
			executarComando(command, file);
			executarComando(pathDoGit + " push", file);
		}
		
	}
	
	private static StringBuilder executarComando(String command) throws Exception {
		StringBuilder executarComando = executarComando(command, new File(""));
		return executarComando;
	}

	private static StringBuilder executarComando(String command, File novoDiretorio) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(novoDiretorio);
        Process processo = builder.start();
        processo.waitFor(); // Aguardar o término do processo, se necessário
        System.out.println("Comando executado no diretório: " + novoDiretorio.getAbsolutePath());		
        InputStream stdIn = processo.getInputStream();
		InputStreamReader isr = new InputStreamReader(stdIn);
		BufferedReader br = new BufferedReader(isr);

		String line = null;
		StringBuilder sb = new StringBuilder(); 

		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		return sb;
	}
}
