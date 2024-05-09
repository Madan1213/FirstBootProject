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

    //Searching option
    const search = () => {
        let query = $('#search-input').val();

        if(query==""){
            $('.search-result').hide()
        }else{
            let url =`http://localhost:8181/smartcontact/search/${query}`
            fetch(url).then(response => response.json())
            .then(data=>{
                let htmlBody = `<div class='list-group'>`

                data.forEach((contact)=>{
                    htmlBody += `<a href='/smartcontact/user/contactDetails/${contact.cid}' class="list-group-item list-group-item-action">${contact.name}</a>`
                });

                htmlBody += `</div>`
                $('.search-result').html(htmlBody);
                $('.search-result').show();
            })

        }
    }

    $('#search-input').on('keyup', function(){
        search();
    });

});