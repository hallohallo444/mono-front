以下、研修課題の起動方法を記載致します。

 - 各種コマンド、gitURLについて
 - 起動手順

〇 各種コマンド、gitURLについて

# クライアントサイド起動コマンド ###########
pnpm dev --open

# サーバサイド起動コマンド ###########
./mvnw clean compile quarkus:dev

# gitURL ###########
[mono-front]
https://github.com/hallohallo444/mono-front.git

[mono-back]
https://github.com/hallohallo444/mono-back.git


〇 起動手順

前提：DB環境として PostgreSQL（ポート：5432）をご用意頂き、
　　　接続可能な状態にして頂きますようお願い申し上げます。
　　　なお、DBの接続設定は以下に記載しております。
　　　mono-back\src\main\resources\application.properties

◆ mono-front編
  ① mono-frontを任意の場所にcloneする（上記gitURL参照）
  ② (windows: powershell / mac: ターミナル を開き) mono-frontディレクトリ直下に移動
  ③ コマンド 「pnpm dev --open」を実行する

◆ mono-back編
  ① mono-backを任意の場所にcloneする（上記gitURL参照）
  ② (windows: powershell / mac: ターミナル を開き) mono-backディレクトリ直下に移動
      ※powershell / ターミナルは新規で開くこと
  ③ コマンド「./mvnw clean compile quarkus:dev」を実行する
