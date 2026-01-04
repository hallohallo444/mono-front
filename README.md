以下、研修課題の起動方法を記載致します。

 * 事前準備
 * 各種コマンド、gitURLについて
 * 起動手順
<br />

## ◆ 事前準備
「Monocrea Technical Training」を抜粋
#### ⚪︎ Required Software

* Chocolatey (Windows Only)
* Homebrew (macOS Only)
* Git
* Java
* Maven
* Node.js
* pnpm
* Docker
* VSCode

<br />
必要なソフトウェアをインストールする。
<br />

### Windows
#### ⚪︎ Chocolatey
Windows PowerShellを管理者として起動し、以下のコマンドを実行する。

`Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = 
[System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
`
<br />

#### ⚪︎ その他
コマンドプロンプトを管理者として起動し、以下のコマンドを実行する。
> choco install -y git <br />
choco install -y corretto21jdk <br />
choco install -y maven <br />
choco install -y docker-desktop <br />
choco install -y vscode <br />
choco install -y pnpm <br />
choco install -y nodejs-lts


### macOS
#### ⚪︎ Homebrew
ターミナルを起動し、以下のコマンドを実行する。

`/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install.sh)"`

#### ⚪︎ その他
ターミナルを起動し、以下のコマンドを実行する。

> brew install git <br />
brew install corretto@21 <br />
brew install maven <br />
brew install docker --cask <br />
brew install visual-studio-code --cask <br />
brew install pnpm <br />
brew install nodebrew

#### ⚪︎ nodebrewのセットアップ

#### 初期設定

> nodebrew setup <br />
echo 'export PATH=$PATH:$HOME/.nodebrew/current/bin' >> ~/.bash_profile

#### 使用するバージョンをセットする

事前にLTSバージョンを確認しておく<br />
(https://github.com/nodejs/Release?tab=readme-ov-file#release-schedule)

nodeのインストール＋使用設定をする
target versionはLTSバージョンに置き換える
> nodebrew install-binary <target version\><br />
nodebrew use <target version\>

#### nodeコマンドでバージョン確認

> node -v

#### ⚪︎ VSCode拡張機能 (Windows、macOS共通)

VSCodeには以下のコマンドで拡張機能をインストールする。<br />
> code --install-extension dbaeumer.vscode-eslint <br />
code --install-extension josevseb.google-java-format-for-vs-code <br />
code --install-extension vscjava.vscode-lombok <br />
code --install-extension mhutchie.git-graph <br />
code --install-extension ms-vscode.js-debug <br />
code --install-extension svelte.svelte-vscode <br />
code --install-extension redhat.vscode-quarkus <br />
code --install-extension sonarsource.sonarlint-vscode <br />
code --install-extension vscjava.vscode-java-pack


## ◆ 各種コマンド、gitURLについて

#### ⚪︎ クライアントサイド起動コマンド
>pnpm dev --open

#### ⚪︎ サーバサイド起動コマンド
>./mvnw clean compile quarkus:dev

#### ⚪︎ gitURL
**[mono-front]**
>https://github.com/hallohallo444/mono-front.git

**[mono-back]**
>https://github.com/hallohallo444/mono-back.git


## ◆ 起動手順

 前提：DB環境として PostgreSQL（ポート：5432）をご用意頂き、<br />接続可能な状態にして頂きますようお願い申し上げます。<br />なお、DBの接続設定は以下に記載しております。
>mono-back\src\main\resources\application.properties

#### ⚪︎ mono-front編
  ① mono-frontを任意の場所にcloneする（上記gitURL参照）<br />
  ② (windows: powershell / mac: ターミナル を開き) mono-frontディレクトリ直下に移動<br />
  ③ コマンド 「pnpm dev --open」を実行する

#### ⚪︎ mono-back編
  ① mono-backを任意の場所にcloneする（上記gitURL参照）<br />
  ② (windows: powershell / mac: ターミナル を開き)
  mono-backディレクトリ直下に移動<br />
      ※powershell / ターミナルは新規で開くこと<br />
  ③ コマンド「./mvnw clean compile quarkus:dev」を実行する
