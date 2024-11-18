package com.ccp.constantes;

public class Teste {
	public static void main(String[] args) {

		for(int k = 0; k<30; k++) {
			
			boolean multiploDe3 = k % 3 == 0;
			boolean multiploDe5 = k % 5 == 0;
			
			if(multiploDe3 == false && multiploDe5 == false) {
				System.out.println(k);
				continue;
			}
			
			if(multiploDe3) {
				System.out.print("Fizz ");
			}
			if(multiploDe5) {
				System.out.print("Buzz ");
			}
			System.out.println();
			
		}
		
	}
}
