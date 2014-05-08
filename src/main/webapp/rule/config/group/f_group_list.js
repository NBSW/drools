/**
 * Created by XuanLubin on 2014/3/28.
 */
var $Group_list = function(){
    var click_cal = undefined;
    var group_map = {};//
    var editMode = false;
    var setClickCal = function(call){
        click_cal = call;
    };
    var changeEditMode = function(enable){
        editMode = enable;
    };
    var updateList = function (selected) {
        var group_list = $('#group_list');
        group_list.empty();
        Q.ajax.get("config/group/1/status", function (data) {
            $.each(data, function (i, ii) {
                var li = '<li code="' + ii.ruleGroupRowId + '">' + (i + 1) + ':' + ii.taskName ;
                if(editMode){
                    li = li + "<a href='####' type='delete'>删除</a>";
                }
                li = li + '</li>';
                var $li = $(li);
                if(editMode){
                    $li.find('>a').bind('click',function(event){
                        var $a = $(this);
                        if($a.attr('type')=="delete"){
                            del($a.parent().attr('code'));
                        }
                        event.preventDefault();
                        event.stopPropagation();
                    });
                }
                group_list.append($li);
                group_map[ii.ruleGroupRowId] = ii;
            });
            group_list.find('>li').bind('click', function () {
                li_click($(this));
            });
            group_list.find('>li:odd').addClass('li_odd');
            if(selected){
                group_list.find('>li[code='+selected+']').click();
            }
        });
    };
   var li_click = function (li) {
        if (!li.hasClass('li_selected')) {
            $('#group_list>li').removeClass('li_selected');
            li.addClass('li_selected');
        }
        if (typeof click_cal == 'function') {
            click_cal(group_map[li.attr('code')]);
        }
    };
    var getSelected = function(){
        var li = $('#group_list>li.li_selected');
        return li.attr('code');
    };

    var del = function(id){
        $.ligerDialog.confirm("确定删除该考核主体?",function(y){
           if(y){
               var selected = getSelected();
               Q.ajax.del("config/group/"+id,function(){
                   updateList(selected);
               })
           }
        });
    };

    return{
        setClickCal:function(call){
            setClickCal(call);
        },
        updateList:function(selected){
            updateList(selected);
        },
        getSelected:function(){
            return getSelected();
        },
        enableEdit:function(){
            changeEditMode(true);
        },
        disableEditMode:function(){
            changeEditMode(false);
        }
    }
}();

var f_group_list = function () {
    if(!$view_mode) {
        $Group_list.enableEdit();
    }else{
        $Group_list.disableEditMode();
    }
    $Group_list.updateList();
};