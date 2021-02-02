#include <stdio.h>
typedef struct {
	int t0;
	int t1;
} int_int;
typedef struct {
	int t0;
	int t1;
	int t2;
} int_int_int;
int_int getPair();
int_int getPairRec();
int add(int, int);
int_int_int three(int, int, int);
int_int getPair()
{
	int_int res;
	res.t0 = 1;
	res.t1 = 1;
	return res;
}
int_int getPairRec()
{
	int_int _getPair_0 = getPair();
	int_int res;
	res.t0 = _getPair_0.t0;
	res.t1 = _getPair_0.t1;
	return res;
}
int add(int a, int b)
{
	printf("returning ");
	printf("%d", a);
	printf(" and ");
	printf("%d", b);
	printf("\n");
	return a + b;
}
int_int_int three(int a, int b, int c)
{
	int r1 = a + b;
	int r2 = b + c;
	int r3 = a + c;
	int_int_int res;
	res.t0 = r1;
	res.t1 = r2;
	res.t2 = r3;
	return res;
}
int main()
{
	int a = 0;
	int b = 0;
	int c = 0;
	add(1, 5);
	int_int _getPairRec_1 = getPairRec();
	int_int _getPair_2 = getPair();
	int_int_int _three_3 = three(_getPairRec_1.t0, _getPairRec_1.t1, add(_getPair_2.t0, _getPair_2.t1) + c);
	int_int _getPairRec_4 = getPairRec();
	int_int _getPair_5 = getPair();
	int_int_int _three_6 = three(_getPairRec_4.t0, _getPairRec_4.t1, add(_getPair_5.t0, _getPair_5.t1) + c);
	a = _three_6.t0;
	b = _three_6.t1;
	c = _three_6.t2;
	c = c + c;
	printf("%d", a);
	printf(", ");
	printf("%d", b);
	printf(", ");
	printf("%d", c);
	return 0;
}
