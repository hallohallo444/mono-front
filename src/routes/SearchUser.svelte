<!-- ユーザー検索画面 -->
<!--
⚪︎詳細仕様
  検索条件: ユーザーID、ユーザー名
  検索結果一覧の詳細リンクをクリックするとユーザー詳細画面に遷移
  検索結果一覧はソート、ページネーションが可能
-->
<link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Zen+Kaku+Gothic+New&display=swap" rel="stylesheet">

<p class="zen-kaku-gothic-new-regular" style="font-size:36px;">ユーザ検索画面</p>

<p style="text-align:right;">
    <a href="/regist" class="button12">新規登録</a>
</p>

<script lang="ts">


    /**
	 * フィールド定義
	 */
    let resultData = $state([]);
    let userid = $state();
    let pageNum = $state(1);
    let username = $state("");
    let resultOnlyData = $state();
    let prevDisabled = $state(true);
    let nextDisabled = $state(true);

    /** ページネーション定義(start) */
    import { PaginationItem } from "flowbite-svelte";
    const previous = () => {
        if (pageNum > 1) {
            pageNum--;
        }
        getData();
    };
    const next = () => {
        pageNum++;
        getData();
    };
    /** ページネーション定義(end) */

	let scoops = $state(1);


	const formatter = new Intl.ListFormat('en', { style: 'long', type: 'conjunction' });


    async function getData() {

        //const url = "http://localhost:8080/search?id=AAA&name=BBB";
        // urlの切り替え
        let urlStr = "";
        let idNum = userid === undefined ? 0 : Number(userid);
        let MAX_PAGE_LENGTH = 10;
        // offset＝レコードの取得開始地点
        let offset = pageNum == 1 ? 1 :pageNum;

        if (pageNum == 1) {
            prevDisabled = true;
        } else {
            prevDisabled = false;
        }
        
        if((idNum !== 0) && (username == null || username == "")){
            urlStr += "/id/" + userid;
        } else if((username !== null || username !== "") && (idNum == 0) ){
            //alert(username);
            urlStr += "/" + username;
        }else if((username !== null || username !== "" )&& (idNum !== 0)){
            //alert("/" + userid + "/" + username);
            urlStr += "/" + idNum + "/" + username;
        }
        const url = "http://localhost:8080/search" + urlStr + "/" + MAX_PAGE_LENGTH + "/" + offset;

        try {
            const response = await fetch(url);

            if (!response.ok) {
            throw new Error(`レスポンスステータス: ${response.status}`);
            }

            const result = await response.json();
            const resultNum = result.searchResultListsNum;
            
            resultData = result.userList;

            if (Array.isArray(resultData)) {
                if(resultData.length !== MAX_PAGE_LENGTH) {
                    nextDisabled = true;
                } else if (pageNum * 10 < resultNum) {
                    nextDisabled = false;
                }
            }
            
        } catch (error) {
            alert("error");
            console.log(error);
            //console.error(error.message);
        }
    }

</script>

<div align="center">
    <table>
        <tbody>
            <tr>
                <th class="zen-kaku-gothic-new-regular">ユーザID</th>
                <th class="zen-kaku-gothic-new-regular">
                    <input bind:value={userid} />
                </th>
            </tr>
            <tr>
                <th class="zen-kaku-gothic-new-regular">ユーザ名</th>
                <th class="zen-kaku-gothic-new-regular">
                    <input bind:value={username} />
                </th>
            </tr>
            <tr>
            </tr>
        </tbody>
    </table>
</div>

<br />

<div align="center" class="buttons-list__single">
    <button class="css-button css-button-fully-rounded css-button-fully-rounded--blue"  onclick={getData}>
     検索 
    </button>
</div>

<br />

<!-- 検索結果出力 -->
<div align="center">
<table border="1" style="border-collapse: collapse">
    <thead>
        <tr>
            <th class="zen-kaku-gothic-new-regular">ユーザーID</th>
            <th colspan="2"style="width:300px;" class="zen-kaku-gothic-new-regular">ユーザー名</th>
        </tr>
    </thead>
    <tbody>
    {#if Array.isArray(resultData)}
        {#each resultData as item}
            <tr class="zen-kaku-gothic-new-regular">
                <td align="center">
                    { item.id }
                </td>
                <td align="center">
                    { item.name }
                </td>
                <td align="center">
                    <a href={`/detail?id=${item.id}`}>詳細情報</a>
                </td>
            </tr>
        {/each}
    {:else}
        <tr>
            <td align="center">
                { resultData.id }
            </td>
            <td align="center">
                { resultData.name }
            </td>
            <td>
                <!--
                <a href="/detail">詳細情報</a>
                -->
                <a href={`/detail?id=${resultData.id}`}>詳細情報</a>
            </td>
        </tr>
    {/if}
    </tbody>
</table>

<!-- ページネーション -->
<br />
<div class="flex space-x-3 rtl:space-x-reverse">
  <PaginationItem onclick={previous} disabled={prevDisabled}>Previous</PaginationItem>
  <PaginationItem onclick={next} disabled={nextDisabled}>Next</PaginationItem>
</div>


</div>