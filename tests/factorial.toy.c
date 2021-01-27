#include <stdio.h>
#include <string.h>

int factorial(int);
int factorial(int n)
{
    int result = 0;
    if (n == 0) {
        result = 1;
    }
    else {
        result = n * factorial(n - 1);
    }
    return result;
}
int main()
{
    int n = 0;
    printf("Enter n, or <= 0 to exit: ");
    scanf("%d", &n);
    while (n > 0) {
        printf("Factorial of ");
        printf("%d", n);
        printf(" is ");
        printf("%d", factorial(n));
        printf("\n");
        n = n - 1;
    }
    return 0;
}
