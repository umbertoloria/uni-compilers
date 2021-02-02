#include <stdio.h>
void sommaNumeri();
void moltiplicaNumeri();
void dividiNaturali();
void elevaAPotenza();
void calcolareSuccessioneFibonacci();
void menu();
int option = 0;
void sommaNumeri()
{
	int a = 0;
	int b = 0;
	printf("Fornisci il primo addendo> ");
	scanf("%d", &a);
	printf("Fornisci il secondo addendo> ");
	scanf("%d", &b);
	printf("Somma: ");
	printf("%d", a + b);
	printf("\n");
}
void moltiplicaNumeri()
{
	int a = 0;
	int b = 0;
	int res = 0;
	int i = 0;
	printf("Fornisci il primo fattore> ");
	scanf("%d", &a);
	printf("Fornisci il secondo fattore> ");
	scanf("%d", &b);
	i = 0;
	while (i < b) {
		res = res + a;
		i = i + 1;
	}
	printf("Moltiplicazione: ");
	printf("%d", res);
	printf("\n");
}
void dividiNaturali()
{
	int a = 0;
	int b = 0;
	printf("Fornisci il dividendo> ");
	scanf("%d", &a);
	printf("Fornisci il divisore> ");
	scanf("%d", &b);
	printf("Rapporto: ");
	printf("%d", a / b);
	printf("\n");
}
void elevaAPotenza()
{
	int a = 0;
	int b = 0;
	int res = 1;
	int i = 0;
	printf("Fornisci la base> ");
	scanf("%d", &a);
	printf("Fornisci l'esponente> ");
	scanf("%d", &b);
	i = 0;
	while (i < b) {
		res = res * a;
		i = i + 1;
	}
	printf("Moltiplicazione: ");
	printf("%d", res);
	printf("\n");
}
void calcolareSuccessioneFibonacci()
{
	int n = 0;
	int i = 0;
	int a = 0;
	int b = 0;
	printf("Fornisci quanti numeri di Fibonacci calcolare> ");
	scanf("%d", &n);
	if (n >= 0) {
		printf("Fibonacci di 0 = 0\n");
		printf("Fibonacci di 1 = 1\n");
		a = 0;
		b = 1;
		i = 2;
		while (i <= n) {
			b = b + a;
			a = b - a;
			printf("Fibonacci di ");
			printf("%d", i);
			printf(" = ");
			printf("%d", b);
			printf("\n");
			i = i + 1;
		}
	}
}
void menu()
{
	printf("Seleziona l'operazione che desideri eseguire\n\n");
	printf("[0] sommare due numeri\n");
	printf("[1] moltiplicare due numeri\n");
	printf("[2] dividere due numeri naturali\n");
	printf("[3] elevare a potenza un numero per una base\n");
	printf("[4] calcolare la successione di Fibonacci\n");
	printf("[*] ...uscire\n\n");
	printf("> ");
	scanf("%d", &option);
}
int main()
{
	menu();
	while (0 <= option && option <= 4) {
		if (option == 0) {
			sommaNumeri();
		}
		else if (option == 1) {
			moltiplicaNumeri();
		}
		else if (option == 2) {
			dividiNaturali();
		}
		else if (option == 3) {
			elevaAPotenza();
		}
		else if (option == 4) {
			calcolareSuccessioneFibonacci();
		}
		printf("\n");
		menu();
	}
	printf("Uscita.\n");
	return 0;
}
