<script>
  import { onMount } from 'svelte';

  // 検索条件
  let searchTerm = $state('');
  let selectedCategory = $state('');
  
  // データ状態
  let items = $state([]);
  let filteredItems = $state([]);
  let loading = $state(false);
  let error = $state('');
  
  // UI状態
  let currentPage = $state(1);
  let itemsPerPage = $state(10);
  let totalItems = $state(0);

  // デバウンス用
  let searchTimeout;

  /**
   * API呼び出し関数
   */
  async function fetchData(searchQuery = '', category = '') {
    loading = true;
    error = '';

    try {
      // クエリパラメータの構築
      const params = new URLSearchParams();
      if (searchQuery) params.append('q', searchQuery);
      if (category) params.append('category', category);
      params.append('_limit', '100'); // JSONPlaceholderの場合

      // 複数のAPIエンドポイントの例
      const apiEndpoint = 'https://jsonplaceholder.typicode.com/posts';
      // const apiEndpoint = 'http://localhost:3000/api/users';
      // const apiEndpoint = 'https://api.example.com/search';

      const url = params.toString() ? `${apiEndpoint}?${params}` : apiEndpoint;
      
      console.log('APIリクエスト:', url);

      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          // 'Authorization': 'Bearer your-token-here', // 認証が必要な場合
        }
      });

      if (!response.ok) {
        throw new Error(`HTTP Error: ${response.status} ${response.statusText}`);
      }

      const data = await response.json();
      
      // データの加工（APIの構造に応じて調整）
      items = data.map(item => ({
        id: item.id,
        title: item.title,
        body: item.body,
        userId: item.userId,
        category: item.category || 'その他', // カテゴリがない場合のデフォルト
        createdAt: item.createdAt || new Date().toISOString().split('T')[0]
      }));

      totalItems = items.length;
      console.log(`データ取得完了: ${totalItems}件`);

    } catch (err) {
      console.error('API Error:', err);
      error = `データの取得に失敗しました: ${err.message}`;
      items = [];
      totalItems = 0;
    } finally {
      loading = false;
    }
  }

  /**
   * 検索実行（デバウンス付き）
   */
  function performSearch() {
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(() => {
      fetchData(searchTerm, selectedCategory);
      currentPage = 1; // 検索時は1ページ目に戻る
    }, 300); // 300ms後に実行
  }

  /**
   * 検索条件のリアクティブな監視
   */
  $effect(() => {
    // searchTermまたはselectedCategoryが変更されたら検索実行
    performSearch();
  });

  /**
   * ローカルフィルタリング（API検索と併用）
   */
  $effect(() => {
    if (!items.length) {
      filteredItems = [];
      return;
    }

    // クライアントサイドでの追加フィルタリング
    filteredItems = items.filter(item => {
      const matchesSearch = !searchTerm || 
        item.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        item.body.toLowerCase().includes(searchTerm.toLowerCase());
      
      const matchesCategory = !selectedCategory || 
        item.category === selectedCategory;
      
      return matchesSearch && matchesCategory;
    });
  });

  /**
   * ページネーション計算
   */
  $: totalPages = Math.ceil(filteredItems.length / itemsPerPage);
  $: startIndex = (currentPage - 1) * itemsPerPage;
  $: endIndex = startIndex + itemsPerPage;
  $: currentPageItems = filteredItems.slice(startIndex, endIndex);

  /**
   * ページ変更
   */
  function goToPage(page) {
    if (page >= 1 && page <= totalPages) {
      currentPage = page;
    }
  }

  /**
   * 検索条件リセット
   */
  function clearSearch() {
    searchTerm = '';
    selectedCategory = '';
  }

  /**
   * 手動リフレッシュ
   */
  function refreshData() {
    fetchData(searchTerm, selectedCategory);
  }

  /**
   * 詳細表示
   */
  function showDetail(item) {
    alert(`詳細情報:\nID: ${item.id}\nタイトル: ${item.title}\n内容: ${item.body.substring(0, 100)}...`);
  }

  // コンポーネント初期化時にデータ取得
  onMount(() => {
    fetchData();
  });

  // カテゴリ一覧の計算
  $: categories = [...new Set(items.map(item => item.category))];
</script>

<div class="search-container">
  <h1>🔍 データ検索システム</h1>
  
  <!-- 検索フォーム -->
  <div class="search-form">
    <div class="search-row">
      <div class="input-group">
        <label for="search">キーワード検索:</label>
        <input
          id="search"
          type="text"
          bind:value={searchTerm}
          placeholder="タイトルまたは内容で検索..."
          class="search-input"
        />
      </div>
      
      <div class="input-group">
        <label for="category">カテゴリ:</label>
        <select id="category" bind:value={selectedCategory} class="search-select">
          <option value="">すべて</option>
          {#each categories as category}
            <option value={category}>{category}</option>
          {/each}
        </select>
      </div>
      
      <div class="button-group">
        <button onclick={clearSearch} class="clear-btn" disabled={loading}>
          クリア
        </button>
        <button onclick={refreshData} class="refresh-btn" disabled={loading}>
          {loading ? '更新中...' : '更新'}
        </button>
      </div>
    </div>
  </div>

  <!-- エラー表示 -->
  {#if error}
    <div class="error-message">
      ⚠️ {error}
      <button onclick={refreshData} class="retry-btn">再試行</button>
    </div>
  {/if}

  <!-- 検索結果ヘッダー -->
  <div class="results-header">
    <div class="results-info">
      <span class="results-count">
        {#if loading}
          検索中...
        {:else}
          検索結果: {filteredItems.length}件
          {#if filteredItems.length > 0}
            ({startIndex + 1}-{Math.min(endIndex, filteredItems.length)}件目を表示)
          {/if}
        {/else}
      </span>
    </div>
    
    {#if filteredItems.length > 0}
      <div class="items-per-page">
        <label>
          表示件数:
          <select bind:value={itemsPerPage}>
            <option value={5}>5件</option>
            <option value={10}>10件</option>
            <option value={20}>20件</option>
            <option value={50}>50件</option>
          </select>
        </label>
      </div>
    {/if}
  </div>

  <!-- 検索結果一覧 -->
  <div class="results-container">
    {#if loading}
      <div class="loading-spinner">
        <div class="spinner"></div>
        <p>データを読み込み中...</p>
      </div>
    {:else if currentPageItems.length > 0}
      <div class="results-grid">
        {#each currentPageItems as item (item.id)}
          <div class="result-card">
            <div class="card-header">
              <h3 class="card-title">{item.title}</h3>
              <span class="card-id">ID: {item.id}</span>
            </div>
            <div class="card-body">
              <p class="card-content">
                {item.body.length > 100 ? item.body.substring(0, 100) + '...' : item.body}
              </p>
              <div class="card-meta">
                <span class="category-tag">{item.category}</span>
                <span class="user-id">User: {item.userId}</span>
                <span class="date">{item.createdAt}</span>
              </div>
            </div>
            <div class="card-actions">
              <button onclick={() => showDetail(item)} class="detail-btn">
                詳細を見る
              </button>
            </div>
          </div>
        {/each}
      </div>

      <!-- ページネーション -->
      {#if totalPages > 1}
        <div class="pagination">
          <button 
            onclick={() => goToPage(currentPage - 1)}
            disabled={currentPage === 1}
            class="page-btn"
          >
            ← 前へ
          </button>
          
          {#each Array.from({length: Math.min(totalPages, 7)}, (_, i) => {
            if (totalPages <= 7) return i + 1;
            if (currentPage <= 4) return i + 1;
            if (currentPage >= totalPages - 3) return totalPages - 6 + i;
            return currentPage - 3 + i;
          }) as page}
            <button 
              onclick={() => goToPage(page)}
              class="page-btn {page === currentPage ? 'active' : ''}"
            >
              {page}
            </button>
          {/each}
          
          <button 
            onclick={() => goToPage(currentPage + 1)}
            disabled={currentPage === totalPages}
            class="page-btn"
          >
            次へ →
          </button>
        </div>
      {/if}
    {:else}
      <div class="no-results">
        <div class="no-results-icon">🔍</div>
        <h3>検索結果が見つかりませんでした</h3>
        <p>検索条件を変更して再度お試しください</p>
        <button onclick={clearSearch} class="clear-search-btn">
          検索条件をリセット
        </button>
      </div>
    {/if}
  </div>
</div>

<style>
  .search-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  }

  h1 {
    color: #333;
    text-align: center;
    margin-bottom: 30px;
    font-size: 28px;
  }

  .search-form {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 25px;
    border-radius: 12px;
    margin-bottom: 30px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  }

  .search-row {
    display: flex;
    gap: 20px;
    align-items: end;
    flex-wrap: wrap;
  }

  .input-group {
    flex: 1;
    min-width: 200px;
  }

  .input-group label {
    display: block;
    margin-bottom: 8px;
    color: white;
    font-weight: 600;
    font-size: 14px;
  }

  .search-input, .search-select {
    width: 100%;
    padding: 12px;
    border: 2px solid transparent;
    border-radius: 8px;
    font-size: 16px;
    background: white;
    transition: all 0.3s ease;
  }

  .search-input:focus, .search-select:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 0 3px rgba(0,123,255,0.25);
  }

  .button-group {
    display: flex;
    gap: 10px;
    align-items: center;
  }

  .clear-btn, .refresh-btn, .retry-btn {
    padding: 12px 20px;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-weight: 600;
    transition: all 0.3s ease;
  }

  .clear-btn {
    background: rgba(255,255,255,0.2);
    color: white;
    border: 2px solid rgba(255,255,255,0.3);
  }

  .clear-btn:hover:not(:disabled) {
    background: rgba(255,255,255,0.3);
  }

  .refresh-btn {
    background: #28a745;
    color: white;
  }

  .refresh-btn:hover:not(:disabled) {
    background: #218838;
    transform: translateY(-2px);
  }

  .refresh-btn:disabled, .clear-btn:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }

  .error-message {
    background: linear-gradient(135deg, #ff6b6b, #ee5a52);
    color: white;
    padding: 15px 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .retry-btn {
    background: rgba(255,255,255,0.2);
    color: white;
    font-size: 12px;
    padding: 8px 16px;
  }

  .results-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }

  .results-count {
    font-weight: 600;
    color: #666;
    font-size: 16px;
  }

  .items-per-page select {
    padding: 6px 10px;
    border: 2px solid #ddd;
    border-radius: 6px;
    background: white;
  }

  .loading-spinner {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 60px;
  }

  .spinner {
    width: 40px;
    height: 40px;
    border: 4px solid #f3f3f3;
    border-top: 4px solid #007bff;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: 15px;
  }

  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }

  .results-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
    gap: 20px;
    margin-bottom: 30px;
  }

  .result-card {
    background: white;
    border-radius: 12px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.08);
    overflow: hidden;
    transition: all 0.3s ease;
  }

  .result-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 25px rgba(0,0,0,0.15);
  }

  .card-header {
    padding: 20px 20px 10px;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
  }

  .card-title {
    margin: 0;
    color: #333;
    font-size: 18px;
    line-height: 1.4;
    flex: 1;
  }

  .card-id {
    background: #f8f9fa;
    color: #666;
    padding: 4px 8px;
    border-radius: 12px;
    font-size: 12px;
    font-weight: 600;
    margin-left: 10px;
  }

  .card-body {
    padding: 0 20px 15px;
  }

  .card-content {
    color: #666;
    line-height: 1.6;
    margin-bottom: 15px;
  }

  .card-meta {
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
    align-items: center;
  }

  .category-tag {
    background: linear-gradient(135deg, #667eea, #764ba2);
    color: white;
    padding: 4px 12px;
    border-radius: 15px;
    font-size: 12px;
    font-weight: 600;
  }

  .user-id, .date {
    color: #999;
    font-size: 12px;
  }

  .card-actions {
    padding: 15px 20px 20px;
    border-top: 1px solid #f0f0f0;
  }

  .detail-btn {
    background: linear-gradient(135deg, #667eea, #764ba2);
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 20px;
    cursor: pointer;
    font-weight: 600;
    transition: all 0.3s ease;
    width: 100%;
  }

  .detail-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(102,126,234,0.4);
  }

  .pagination {
    display: flex;
    justify-content: center;
    gap: 5px;
    margin-top: 30px;
  }

  .page-btn {
    padding: 10px 15px;
    border: 2px solid #ddd;
    background: white;
    color: #666;
    cursor: pointer;
    border-radius: 8px;
    font-weight: 600;
    transition: all 0.3s ease;
  }

  .page-btn:hover:not(:disabled) {
    border-color: #007bff;
    color: #007bff;
  }

  .page-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .page-btn.active {
    background: linear-gradient(135deg, #667eea, #764ba2);
    color: white;
    border-color: transparent;
  }

  .no-results {
    text-align: center;
    padding: 60px 20px;
    color: #666;
  }

  .no-results-icon {
    font-size: 48px;
    margin-bottom: 20px;
  }

  .no-results h3 {
    margin: 0 0 10px;
    color: #333;
  }

  .clear-search-btn {
    background: #007bff;
    color: white;
    border: none;
    padding: 12px 24px;
    border-radius: 25px;
    cursor: pointer;
    font-weight: 600;
    margin-top: 20px;
    transition: all 0.3s ease;
  }

  .clear-search-btn:hover {
    background: #0056b3;
    transform: translateY(-2px);
  }

  @media (max-width: 768px) {
    .search-row {
      flex-direction: column;
    }
    
    .button-group {
      justify-content: center;
      width: 100%;
    }
    
    .results-header {
      flex-direction: column;
      gap: 15px;
      align-items: flex-start;
    }
    
    .results-grid {
      grid-template-columns: 1fr;
    }
    
    .pagination {
      flex-wrap: wrap;
    }
  }
</style>