function visit(id,title,url){
    var crossDomain = new CrossDomain();
    //初始化跨域类，传入目标域的跨域跳转页面URL
    crossDomain.visit('parent.MenuTabProcessInst.addTab({id:"'+'sfz_'+id+'",title:"'+title+'",url:"'+url+'",sub:true,closable:true})');
}