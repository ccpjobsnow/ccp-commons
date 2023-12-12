package com.ccp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class ExecutaScripts {
	public static void main(String[] args) throws IOException, InterruptedException {

		String mensagemDeCommit = JOptionPane.showInputDialog("Digite sua mensagem de commit.");
		StringBuilder respostaDoAdd = executarComando("git add .");
		System.out.println(respostaDoAdd);
		String command = "git commit -m \""
				+ mensagemDeCommit
				+ "\"";
		StringBuilder respostaDoCommit = executarComando(command);
		System.out.println(respostaDoCommit);
		StringBuilder respostaDoPush = executarComando("git push");
		System.out.println(respostaDoPush);
		
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
