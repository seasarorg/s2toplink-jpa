Derbyテストデータ設定方法

1. org.apache.derby.tools.ijを実行
2. コマンドラインから「connect 'jdbc:derby:testdata;create=true';」と入力してリターン。
3. db/createdb_derby.sqlの内容をコマンドラインにコピペ
（多分ファイルを直接読めると思うのですがまだ調べてません・・・）
4. コマンドラインから「exit;」を実行