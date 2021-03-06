package com.ccp.decorators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CcpFileDecorator {
	public final String content;

	protected CcpFileDecorator(String content) {
		this.content = content;
	}

	public void zip() {
		
		File fileToZip = new File(this.content);
		
		String fileName = fileToZip.getName();
		
		try(FileOutputStream fos = new FileOutputStream(fileName + ".zip");ZipOutputStream zipOut = new ZipOutputStream(fos);) {
			this.zip(fileToZip, zipOut);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//ZipDecorator zipFile()
    private void zip(File fileToZip, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            String terminacao = "/";
        	if (this.content.endsWith("/")) {
        		terminacao = ""; 
            } 
            ZipEntry e = new ZipEntry(this.content + terminacao);
			zipOut.putNextEntry(e);
            zipOut.closeEntry();
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                new CcpFileDecorator(this.content + "/" + childFile.getName()).zip(childFile, zipOut);
            }
            return;
        }
        try(FileInputStream fis = new FileInputStream(fileToZip)) {
            ZipEntry zipEntry = new ZipEntry(this.content);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
	public  String extractStringContent(String... paths) {
		for (String path : paths) {
			File file = new File(path);
			
			if(file.exists() == false) {
				continue;
			}
			if(file.isDirectory() == false) {
				continue;
			}
			
			String pathname = path+ "\\" + this.content;
			long length = new File(pathname).length();
			
			if(length > 1000000) {
				throw new RuntimeException("Arquivo maior que 1 milhao de bytes. Path: " + pathname + ". Bytes: " + length);
			}
			
			String readFileToString= new CcpFileDecorator(pathname).extractStringContent();
			return readFileToString;
		}
		throw new RuntimeException("Nenhum dos arquivos existe. Arquivos: " + Arrays.asList(paths));
	}	
	private String extractStringContent() {
		String path = this.content;
		if(new File(this.content).exists()== false && this.content.toLowerCase().startsWith("..\\") == false && this.content.toLowerCase().startsWith("c:\\") == false) {
			path = "/home/onias/logs" + this.content;
		}
		String line;
		StringBuilder sb = new StringBuilder();
		try (FileReader fr = new FileReader(path); BufferedReader br = new BufferedReader(fr)) {
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}
	public  void append(String content) {
		try {
			File file = new File(this.content);
			if (file.exists() == false) {
				file.createNewFile();
			}
			byte[] bytes = (content + "\n").getBytes();
			Files.write(Paths.get(this.content), bytes, StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public void reset() {

		File f = new File(this.content);
		f.delete();
		try {
			f.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public List<String> getLines(){
		String filePath = this.content;
		if(this.content.toLowerCase().startsWith("c:\\") == false) {
			filePath = "/home/onias/logs" + filePath;
		}
		ArrayList<String> linesFromFile = new ArrayList<>();
		String line;
		try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
			while ((line = br.readLine()) != null) {
				linesFromFile.add(line);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return linesFromFile;
	}
	public static interface FileLineReader {
		void onRead(String fileLine, int lineNumber);
	}

	//FileDecorator readLinesFromFile()
	public  void readLines( FileLineReader reader){
		String line;
		try (FileReader fr = new FileReader(this.content); BufferedReader br = new BufferedReader(fr)) {
			int k = 0;
			while ((line = br.readLine()) != null) {
				reader.onRead(line, k++);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
