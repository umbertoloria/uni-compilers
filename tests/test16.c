#include <stdio.h>
#include <string.h>

int main()
{
	int a = 0;
	int b = 0;
	int cond = 0;
	printf("Fornisci un numero: ");
	scanf("%d", &a);
	printf("Fornisci un altro numero: ");
	scanf("%d", &b);
	if (a > b) {
		printf("Il primo è maggiore\n");
	}
	else {
		printf("Il secondo è maggiore\n");
	}
	if (a > 0 || b > 0) {
		printf("Almeno un numero è positivo\n");
	}
	else {
		printf("Tutti i numeri sono negativi\n");
	}
	cond = a > 0 && b > 0;
	if (!(cond) == 0) {
		printf("Tutti i numeri sono positivi\n");
	}
	else {
		printf("Almeno un numero è negativo\n");
	}
	if (a >= b) {
		printf("Il primo numero è maggiore o uguale del secondo\n");
	}
	else if (a <= b) {
		printf("Il secondo numero non è minore o uguale del primo, quindi è maggiore\n");
	}
	if (a < 3.14159) {
		printf("Il primo numero è minore di Pi greco\n");
	}
	return 0;
}
