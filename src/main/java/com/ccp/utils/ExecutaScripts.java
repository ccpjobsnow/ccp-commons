package com.ccp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class ExecutaScripts {
	public static void main(String[] args) throws IOException, InterruptedException {

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
			String absolutePath = file.getAbsolutePath().replace("\\", "/");
			System.setProperty("user.dir", absolutePath);			
			executarComando("git add .");
			String command = "git commit -m \""
					+ mensagemDeCommit
					+ "\"";
			executarComando(command);
			executarComando("git push");
		}
		
	}

	private static StringBuilder executarComando(String command) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(command);
		InputStream stdIn = proc.getInputStream();
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
