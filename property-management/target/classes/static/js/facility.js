document.addEventListener('DOMContentLoaded', function() {
    // 编辑设施模态框数据绑定
    const editFacilityModal = document.getElementById('editFacilityModal');
    if (editFacilityModal) {
        editFacilityModal.addEventListener('show.bs.modal', function(event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const name = button.getAttribute('data-name');
            const description = button.getAttribute('data-description');
            const status = button.getAttribute('data-status');
            const location = button.getAttribute('data-location');

            const modal = this;
            modal.querySelector('form').action = `/facilities/${id}`;
            modal.querySelector('#editName').value = name;
            modal.querySelector('#editDescription').value = description || '';
            modal.querySelector('#editStatus').value = status;
            modal.querySelector('#editLocation').value = location;
        });
    }

    // 删除设施模态框数据绑定
    const deleteFacilityModal = document.getElementById('deleteFacilityModal');
    if (deleteFacilityModal) {
        deleteFacilityModal.addEventListener('show.bs.modal', function(event) {
            const button = event.relatedTarget;
            const id = button.getAttribute('data-id');
            const name = button.getAttribute('data-name');

            const modal = this;
            modal.querySelector('#facilityNameToDelete').textContent = name;
            modal.querySelector('form').action = `/facilities/${id}`;
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