#include <stdio.h>
#include <string.h>
typedef struct {
	int t0;
	int t1;
	int t2;
} int_int_int;
int_int_int multAddDiff();
void writeNewLines(int);
char* nome = "Michele";
int_int_int multAddDiff()
{
	int primo = 0;
	int secondo = 0;
	int mul = 0;
	int add = 0;
	int diff = 0;
	printf("Inserire il primo argomento:\n");
	scanf("%d", &primo);
	printf("Inserire il secondo argomento:\n");
	scanf("%d", &secondo);
	mul = primo * secondo;
	add = primo + secondo;
	diff = primo - secondo;
	int_int_int res;
	res.t0 = mul;
	res.t1 = add;
	res.t2 = diff;
	return res;
}
void writeNewLines(int n)
{
	while (n > 0) {
		printf("\n");
		n = n - 1;
	}
}
int main()
{
	int a = 0;
	int b = 0;
	int c = 0;
	int_int_int _multAddDiff_0 = multAddDiff();
	a = _multAddDiff_0.t0;
	b = _multAddDiff_0.t1;
	c = _multAddDiff_0.t2;
	printf("Ciao ");
	printf(nome);
	writeNewLines(2);
	printf("I tuoi valori sono:\n");
	printf("%d", a);
	printf(" per la moltiplicazione\n");
	printf("%d", b);
	printf(" per la somma, e \n");
	printf("%d", c);
	printf(" per la differenza");
	return 0;
}
