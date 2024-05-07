$(document).ready(function(){

    $(document).on('click','#example .delete-contact',  function(){
        let cid = $(this).attr('id');
        console.log('cid '+cid)
        $.confirm({
           title:'Delete Contact',
           content:'Are you sure you want to delete contact',
           type:'red',
           icon:'fa fa-trash',
           buttons:{
                submit:{
                    text:'Delete',
                    btnClass:'btn btn-danger',
                    keys:['enter'],
                    action:function(){
                        fetch('/smartcontact/user/deleteContact/'+cid,{
                            method:'get'
                        }).then(response=>response.json())
                        .then(data =>{});
                        window.location.reload();
                    }
                },
                close:{
                    text:'Close',
                    btnClass:'btn btn-warning',
                    keys:['esc','space'],
                    action:function(){}
                }
           }
        });
    });

});