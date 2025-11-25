<!-- ユーザー検索画面 -->
<!--
⚪︎詳細仕様
  検索条件: ユーザーID、ユーザー名
  検索結果一覧の詳細リンクをクリックするとユーザー詳細画面に遷移
  検索結果一覧はソート、ページネーションが可能
-->

<script lang="ts">
  export let data; // ← load() の return 値がここに渡される

  // 更新
  async function updateData(id: number, name: string) {
    console.log("更新対象ID:", id);
    console.log("更新対象名:", name);

        // urlの切り替え
        let urlStr = "";
        let idNum = Number(id);
        
        urlStr += idNum + "/" + name;
        const url = "http://localhost:8080/search/update/" + urlStr;

        try {
            const response = await fetch(url);

            if (!response.ok) {
            throw new Error(`レスポンスステータス: ${response.status}`);
            }
            alert("更新処理が完了しました。");
            const result = await response.json();

        } catch (error) {
            alert("更新処理が失敗しました。");
            console.log(error);
            //console.error(error.message);
        }
            
    }

  // 削除
  async function deleteData(id: number) {
    console.log("更新対象ID:", id);

        // urlの切り替え
        let urlStr = "";
        let idNum = Number(id);
        
        urlStr += idNum;
        const url = "http://localhost:8080/search/delete/" + urlStr;

        try {
            const response = await fetch(url);

            if (!response.ok) {
            throw new Error(`レスポンスステータス: ${response.status}`);
            }
            alert("削除処理が完了しました。");
            const result = await response.json();

        } catch (error) {
            alert("削除処理が失敗しました。");
            console.log(error);
            //console.error(error.message);
        }
            
    }


</script>


<p class="zen-kaku-gothic-new-regular" style="font-size:36px;">ユーザ詳細画面</p>

<div align="center">
    <table>
        <tbody>
            <tr>
                <th class="zen-kaku-gothic-new-regular">ユーザID</th>
                <th>
                   <p>{data.id}</p>
                    <input bind:value={data.data.id} />
                </th>
            </tr>
            <tr>
                <th class="zen-kaku-gothic-new-regular">ユーザ名</th>
                <th>
                   <p>{data.name}</p>
                    <input bind:value={data.data.name} />
                </th>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <button on:click={() => updateData(data.data.id, data.data.name)}>
                    更新
                    </button>
                    <button on:click={() => deleteData(data.data.id)}>
                    削除
                    </button>
                </td>
            </tr>
        </tbody>
    </table>
</div>

<a href="./../">戻る</a>