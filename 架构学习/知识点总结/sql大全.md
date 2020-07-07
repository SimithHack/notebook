## 排序
```sql
SELECT name, jersey_num FROM roster ORDER BY jersey_num+0;
```

* `jersey_num`是字符串类型，但是，我们想以它的数值大小进行排序

```sql
SELECT t, srcuser, CONCAT(FLOOR((size+1023)/1024),'K') AS size_in_K FROM mail WHERE size > 50000 ORDER BY size;
```

* 以`size`排序，但是，显示的时候用另外的格式

```sql
CREATE TABLE str_val
(
	ci_str CHAR(3) CHARACTER SET latin1 COLLATE latin1_swedish_ci,
	cs_str CHAR(3) CHARACTER SET latin1 COLLATE latin1_general_cs,
	bin_str BINARY(3)
);
' 
表中的数据
+--------+--------+---------+
| ci_str | cs_str | bin_str |
+--------+--------+---------+
| AAA | AAA | AAA |
| aaa | aaa | aaa |
| bbb | bbb | bbb |
| BBB | BBB | BBB |
+--------+--------+---------+
'
SELECT ci_str FROM str_val ORDER BY ci_str;
'
+--------+
| ci_str |
+--------+
| AAA |
| aaa |
| bbb |
| BBB |
+--------+
'
SELECT cs_str FROM str_val ORDER BY cs_str;
'
+--------+
| cs_str |
+--------+
| AAA |
| aaa |
| BBB |
| bbb |
+--------+
'
SELECT bin_str FROM str_val ORDER BY bin_str;
'
+---------+
| bin_str |
+---------+
| AAA |
| BBB |
| aaa |
| bbb |
+---------+
'
```

* `binary string`可以区分大小写排序，它只认byte的字面数值，而不管字符串用的什么字符集或者什么大小写。

* `binary string` 与 `none binary string` 比较，两边都转换为 `binary string` 进行比较