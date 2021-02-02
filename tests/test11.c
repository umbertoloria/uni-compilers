#include <stdio.h>
typedef struct {
	const char* t0;
	const char* t1;
} string_string;
string_string x(float, int, float);
string_string x(float a, int b, float c)
{
	string_string res;
	res.t0 = "inizio";
	res.t1 = "fine";
	return res;
}
int main()
{
	int n = 1;
	int k = 1;
	float a = 0;
	float b = 0;
	const char* str1 = "";
	const char* str2 = "";
	a = n * k;
	string_string _x_0 = x(1, k, n + k);
	str1 = _x_0.t0;
	str2 = _x_0.t1;
	b = 7;
	printf(str1);
	printf(str2);
	return 0;
}
