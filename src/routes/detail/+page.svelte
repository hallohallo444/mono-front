<script lang="ts">
  import UserInfoDetail from './UserInfoDetail.svelte';
  import './../design.css';
  export let data: { id: number; name: string }; // ← load() の return 値がここに渡される
</script>

<UserInfoDetail {data} />