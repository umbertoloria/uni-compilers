#include <stdio.h>
int main()
{
	int i = 1;
	do {
		int i = 0;
		printf("i: ");
		printf("%d", i);
		printf("\n");
		i = i - 1;
	} while (i > 0);
	return 0;
}
