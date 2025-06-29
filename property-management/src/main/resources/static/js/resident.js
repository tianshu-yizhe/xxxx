document.addEventListener('DOMContentLoaded', function() {
    // 编辑住户模态框数据绑定
    const editResidentModal = document.getElementById('editResidentModal');
    if (editResidentModal) {
        editResidentModal.addEventListener('show.bs.modal', function(event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const name = button.getAttribute('data-name');
            const phone = button.getAttribute('data-phone');
            const roomNumber = button.getAttribute('data-roomNumber');
            const identityNumber = button.getAttribute('data-identityNumber');

            const modal = this;
            modal.querySelector('form').action = `/residents/${id}`;
            modal.querySelector('#editName').value = name;
            modal.querySelector('#editPhone').value = phone;
            modal.querySelector('#editRoomNumber').value = roomNumber;
            modal.querySelector('#editIdentityNumber').value = identityNumber;
        });
    }

    // 删除住户模态框数据绑定
    const deleteResidentModal = document.getElementById('deleteResidentModal');
    if (deleteResidentModal) {
        deleteResidentModal.addEventListener('show.bs.modal', function(event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const name = button.getAttribute('data-name');

            const modal = this;
            modal.querySelector('#residentNameToDelete').textContent = name;
            modal.querySelector('form').action = `/residents/${id}`;
        });
    }

    // 表单验证
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
});