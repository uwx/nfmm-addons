$(function() {
    $(document).on('click', function(e) {
        $(e.target).closest('.spoiler,.spoiler_content').filter('.spoiler').find('.spoiler_content:first,.spoiler_closed:first').toggleClass('hidden')
    })
});
