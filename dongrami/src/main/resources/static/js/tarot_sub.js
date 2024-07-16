document.addEventListener('DOMContentLoaded', function() {
    const resultButton = document.getElementById('showResultButton');

    resultButton.addEventListener('click', function(e) {
        e.preventDefault();

        const subcategoryId = parseInt(document.getElementById('subcategoryId').value);
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/result';

        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'subcategoryId';
        input.value = subcategoryId;

        form.appendChild(input);
        document.body.appendChild(form);
        form.submit();
    });
});
