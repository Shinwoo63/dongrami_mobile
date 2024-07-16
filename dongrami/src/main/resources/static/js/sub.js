document.querySelectorAll('a[data-category-id]').forEach(link => {
    link.addEventListener('click', function(e) {
        e.preventDefault();
        const categoryId = this.getAttribute('data-category-id');
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = this.href;
        const hiddenField = document.createElement('input');
        hiddenField.type = 'hidden';
        hiddenField.name = 'categoryId';
        hiddenField.value = categoryId;
        form.appendChild(hiddenField);
        document.body.appendChild(form);
        form.submit();
    });
});
