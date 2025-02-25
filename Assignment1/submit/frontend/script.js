document.querySelector('.tab-nav').addEventListener('click',  function(e) {
    const tabItem = e.target.closest('.tab-item'); 
    if (!tabItem) return;
 
    //全部清除acrive状态
    document.querySelectorAll('.tab-item,  .content-panel').forEach(el => {
        el.classList.remove('active'); 
    });
 
    //标签页active
    tabItem.classList.add('active'); 

    //将当前的active状态添加到对应的标签内容中
    const targetId = tabItem.dataset.target; 
    document.getElementById(targetId).classList.add('active'); 
});