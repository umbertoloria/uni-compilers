#include <stdio.h>
#include <string.h>
typedef struct {
	int t0;
	char* t1;
	float t2;
} int_string_float;
int _enum_0(int);
int_string_float _continue_1(int);
int _enum_0(int _case_2)
{
	return 0;
}
int_string_float _continue_1(int _case_2)
{
	int_string_float res;
	res.t0 = 1;
	res.t1 = "zero";
	res.t2 = _enum_0(_case_2);
	return res;
}
int main()
{
	int _break_3 = 1;
	int _goto_4 = 0;
	float bool_bool = 3 + _break_3 * 2;
	int _extern_5 = 1;
	char* _int_string_float_6 = "";
	printf("%d", _break_3);
	printf("%d", _goto_4);
	printf("%f", bool_bool);
	printf("%d", _extern_5);
	printf(_int_string_float_6);
	int_string_float __continue_1_7 = _continue_1(1);
	printf("%d", __continue_1_7.t0);
	printf(__continue_1_7.t1);
	printf("%f", __continue_1_7.t2);
	int_string_float __continue_1_8 = _continue_1(1);
	_break_3 = __continue_1_8.t0;
	_int_string_float_6 = __continue_1_8.t1;
	bool_bool = __continue_1_8.t2;
	printf("%d", _break_3);
	printf(_int_string_float_6);
	printf("%f", bool_bool);
	return 0;
}
