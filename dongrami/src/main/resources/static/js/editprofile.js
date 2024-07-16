
    document.addEventListener('DOMContentLoaded', function () {
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('updated') === 'true') {
            showUpdateModal();
        }
    });

    function showUpdateModal() {
        $('#updateModal').modal('show');
    }

    function showDeleteModal() {
        $('#deleteModal').modal('show');
    }

    function confirmDelete() {
        document.getElementById('deleteForm').submit();
    }
