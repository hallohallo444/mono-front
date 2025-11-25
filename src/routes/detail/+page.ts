import type { PageLoad } from './$types';

export const load: PageLoad = async ({ url }) => {
  const id = url.searchParams.get('id');
  const res = await fetch(`http://localhost:8080/search/id/${id}`);
  const data = await res.json();

  return { data };
};