### 基础用法示例
+ 安装
    ```sbtshell
    pip install mysql-connector-python
    ```
    
+ 建立连接
    ```python
    import mysql.connector
    
    con = mysql.connector.connect({
        "user":"root",
        "password": "pwd@123!",
        "database": "lowvoltage_visualization",
        "use_unicode": True,
        "host": "172.16.96.136",
        "port": 3306
    })
    cursor = con.cursor()
    cursor.execute("select * from department where id=%d", 1)
    for c in cursor:
        print(c)
    cursor.commit()
    con.close()
    ```
    