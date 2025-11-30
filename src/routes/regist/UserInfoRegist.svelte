<!-- ユーザー検索画面 -->
<!--
⚪︎詳細仕様
  検索条件: ユーザーID、ユーザー名
  検索結果一覧の詳細リンクをクリックするとユーザー詳細画面に遷移
  検索結果一覧はソート、ページネーションが可能
-->
<p class="zen-kaku-gothic-new-regular" style="font-size:36px;">ユーザ新規登録画面</p>


<script>

	let scoops = $state(1);
    let isNotShowPopupMsg = false;
    var result;
    let username = $state("");
    let HOME_LINK = "http://localhost:5173";

	const formatter = new Intl.ListFormat('en', { style: 'long', type: 'conjunction' });


    async function regist() {

        if(username == "" || username == null){
          alert("入力がありません");
          return;
        }
        const url = "http://localhost:8080/search/create/" + username;

        try {
            const response = await fetch(url, {
            method: 'GET', // 必要に応じてPOSTなどに変更
            headers: {
                'Content-Type': 'application/json',
            }
        });

            if (!response.ok) {
            throw new Error(`レスポンスステータス: ${response.status}`);
            }

            if (isNotShowPopupMsg) {
                alert("登録が完了しました。");
                return;
            }
            var warnings = ['登録が完了しました。登録を続行しますか？', '※「OK」押下後は、連続登録が可能ですが、手動で戻る必要があります'];
            result = window.confirm(warnings.join('\n'));

            if( result ) {
                isNotShowPopupMsg = true;
            }
            else {
                location.href = HOME_LINK;
            }

        } catch (error) {
            console.log(error);
        }
    }

</script>

<div align="center">
    <table>
        <tbody>
            <tr>
                <th class="zen-kaku-gothic-new-regular">ユーザ名</th>
                <th>
                    <input bind:value={username} />
                </th>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <button onclick={regist}>
                    登録
                    </button>
                </td>
            </tr>
        </tbody>
    </table>
</div>


<a href="./../">戻る</a>