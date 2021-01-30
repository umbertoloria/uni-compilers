#include <stdio.h>
#include <string.h>

float add(float, float);
float sub(float, float);
float mul(float, float);
float div(float, float);
int yesOrNo = 1;
float add(float x, float y)
{
	return sub(x, -(y));
}
float sub(float x, float y)
{
	return x - y;
}
float mul(float x, float y)
{
	return x * y;
}
float div(float x, float y)
{
	float result = 0;
	if (y == 0) {
		printf("Impossibile dividere per zero");
		result = 0;
	}
	else {
		result = x / y;
	}
	return result;
}
int main()
{
	float a = 0;
	float b = 0;
	float res = 0;
	int kind = 0;
	printf("pre");
	while (yesOrNo) {
		printf("\n[1] Addizione\n");
		printf("[2] Sottrazione\n");
		printf("[3] Multiplicazione\n");
		printf("[4] Divisione\n\n> ");
		scanf("%d", &kind);
		printf("\nFornisci il primo numero: ");
		scanf("%f", &a);
		printf("Fornisci il secondo numero: ");
		scanf("%f", &b);
		if (kind == 1) {
			printf("Resultato: ");
			printf("%f", add(a, b));
		}
		else if (kind == 2) {
			printf("Resultato: ");
			printf("%f", sub(a, b));
		}
		else if (kind == 3) {
			printf("Resultato: ");
			printf("%f", mul(a, b));
		}
		else if (kind == 4) {
			printf("Resultato: ");
			printf("%f", div(a, b));
		}
		printf("\nVuoi continuare? (1 si, 0 no): ");
		scanf("%d", &yesOrNo);
	}
	return 0;
}
