// --- LOGIN, LOGOUT, AND UTILITY FUNCTIONS ---

/**
 * Handles the login process for all user roles.
 * @param {Event} event - The form submission event.
 */
async function login(event) {
    event.preventDefault(); // Prevent default form submission
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const role = document.getElementById('role').value;

    let url = '';
    let body = '';

    if (role === 'STUDENT') {
        url = '/sms/student/login';
        body = `rollNumber=${username}&password=${password}`;
    } else if (role === 'TEACHER') {
        url = '/sms/teacher/login';
        body = `username=${username}&password=${password}`;
    } else if (role === 'ADMIN') {
        url = '/sms/admin/login';
        body = `username=${username}&password=${password}`;
    }

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: body
        });

        if (response.ok) {
            const data = await response.json();
            sessionStorage.setItem('token', data.token);
            sessionStorage.setItem('username', username); // Store username/rollNumber for dashboard fetches

            // Redirect to the appropriate dashboard
            if (role === 'STUDENT') window.location.href = 'student_dashboard.html';
            else if (role === 'TEACHER') window.location.href = 'teacher_dashboard.html';
            else if (role === 'ADMIN') window.location.href = 'admin_dashboard.html';

        } else {
            document.getElementById('message').innerText = 'Invalid credentials';
        }
    } catch (error) {
        console.error('Login failed:', error);
        document.getElementById('message').innerText = 'An error occurred. Please try again.';
    }
}

function logout() {
    sessionStorage.clear();
    window.location.href = 'index.html';
}

function updatePlaceholder() {
    const role = document.getElementById('role').value;
    const usernameInput = document.getElementById('username');
    const label = document.querySelector('label[for="username"]');
    if (role === 'STUDENT') {
        usernameInput.placeholder = 'Enter Roll Number';
        label.textContent = 'Roll Number';
    } else {
        usernameInput.placeholder = 'Enter Username';
        label.textContent = 'Username';
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const roleSelect = document.getElementById('role');
    if (roleSelect) {
        roleSelect.addEventListener('change', updatePlaceholder);
        updatePlaceholder();
    }
});


// --- MODAL (POP-UP FORM) MANAGEMENT ---

function openModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (!modal) return;
    modal.style.display = 'none';
    const form = modal.querySelector('form');
    if (form) {
        form.reset();
        const readOnlyFields = form.querySelectorAll('[readOnly]');
        readOnlyFields.forEach(field => field.readOnly = false);
    }
    const title = modal.querySelector('.modal-title');
    if (title && title.dataset.defaultTitle) {
        title.innerText = title.dataset.defaultTitle;
    }
}


// --- ADMIN DASHBOARD ---

let allStudents = [];

async function fetchAdminDashboard() {
    const token = sessionStorage.getItem('token');
    if (!token) { logout(); return; }

    try {
        const response = await fetch('/admin/dashboard', { headers: { 'Authorization': `Bearer ${token}` } });
        if (!response.ok) { logout(); return; }

        const data = await response.json();
        allStudents = data.students;

        const teachersTableBody = document.getElementById('teachers-table-body');
        teachersTableBody.innerHTML = '';
        data.teachers.forEach(teacher => {
            teachersTableBody.innerHTML += `<tr>
                <td>${teacher.username}</td>
                <td>${teacher.subject}</td>
                <td>${teacher.position || 'N/A'}</td>
                <td>
                    <button class="action-btn view-btn" onclick="viewDetails('teacher', '${teacher.username}')" title="View Details"><i class="fas fa-eye"></i></button>
                    <button class="action-btn assign-btn" onclick="openAssignStudentModal('${teacher.username}')" title="Assign Student"><i class="fas fa-user-plus"></i></button>
                    <button class="action-btn edit-btn" onclick="openEditTeacherModal('${teacher.username}')" title="Edit"><i class="fas fa-edit"></i></button>
                    <button class="action-btn delete-btn" onclick="deleteUser('teacher', '${teacher.username}')" title="Delete"><i class="fas fa-trash"></i></button>
                </td>
            </tr>`;
        });

        const studentsTableBody = document.getElementById('students-table-body');
        studentsTableBody.innerHTML = '';
        data.students.forEach(student => {
            studentsTableBody.innerHTML += `<tr>
                <td>${student.rollNumber}</td>
                <td>${student.username}</td>
                <td>${student.course}</td>
                <td>${student.batch}</td>
                <td>
                    <button class="action-btn view-btn" onclick="viewDetails('student', '${student.rollNumber}')" title="View Details"><i class="fas fa-eye"></i></button>
                    <button class="action-btn edit-btn" onclick="openEditStudentModal('${student.rollNumber}')" title="Edit"><i class="fas fa-edit"></i></button>
                    <button class="action-btn delete-btn" onclick="deleteUser('student', '${student.rollNumber}')" title="Delete"><i class="fas fa-trash"></i></button>
                </td>
            </tr>`;
        });
    } catch (error) {
        console.error('Failed to fetch admin dashboard data:', error);
    }
}

// --- ADMIN USER MANAGEMENT ---

function openCreateTeacherModal() {
    closeModal('teacherModal');
    const title = document.getElementById('teacherModal').querySelector('.modal-title');
    title.innerText = title.dataset.defaultTitle;
    document.getElementById('teacherId').value = '';
    document.getElementById('teacherPassword').required = true;
    document.getElementById('teacherUsername').readOnly = false;
    openModal('teacherModal');
}

function openCreateStudentModal() {
    closeModal('studentModal');
    const title = document.getElementById('studentModal').querySelector('.modal-title');
    title.innerText = title.dataset.defaultTitle;
    document.getElementById('studentId').value = '';
    document.getElementById('studentPassword').required = true;
    document.getElementById('studentRollNumber').readOnly = false;
    openModal('studentModal');
}

async function openEditTeacherModal(username) {
    const token = sessionStorage.getItem('token');
    const response = await fetch(`/admin/teacher/${username}`, { headers: { 'Authorization': `Bearer ${token}` } });
    if (!response.ok) { alert('Failed to fetch teacher details.'); return; }
    const teacher = await response.json();

    const title = document.getElementById('teacherModal').querySelector('.modal-title');
    title.innerText = 'Edit Teacher';
    document.getElementById('teacherId').value = teacher.username;
    document.getElementById('teacherUsername').value = teacher.username;
    document.getElementById('teacherUsername').readOnly = true;
    document.getElementById('teacherSubject').value = teacher.subject;
    document.getElementById('teacherPosition').value = teacher.position; // Corrected to lowercase 'position'
    document.getElementById('teacherOffice').value = teacher.office;
    document.getElementById('teacherPassword').required = false;
    openModal('teacherModal');
}

async function openEditStudentModal(rollNumber) {
    const token = sessionStorage.getItem('token');
    const response = await fetch(`/admin/student/${rollNumber}`, { headers: { 'Authorization': `Bearer ${token}` } });
    if (!response.ok) { alert('Failed to fetch student details.'); return; }
    const student = await response.json();

    const title = document.getElementById('studentModal').querySelector('.modal-title');
    title.innerText = 'Edit Student';
    document.getElementById('studentId').value = student.rollNumber;
    document.getElementById('studentRollNumber').value = student.rollNumber;
    document.getElementById('studentRollNumber').readOnly = true;
    document.getElementById('studentUsername').value = student.username;
    document.getElementById('studentCourse').value = student.course;
    document.getElementById('studentBatch').value = student.batch;
    document.getElementById('studentCgpa').value = student.cgpa;
    document.getElementById('studentPassword').required = false;
    openModal('studentModal');
}

async function handleTeacherSubmit(event) {
    event.preventDefault();
    const id = document.getElementById('teacherId').value;
    const url = id ? `/admin/teacher/${id}` : '/admin/teacher';
    const method = id ? 'PUT' : 'POST';

    const teacherData = {
        username: document.getElementById('teacherUsername').value,
        password: document.getElementById('teacherPassword').value,
        subject: document.getElementById('teacherSubject').value,
        position: document.getElementById('teacherPosition').value, // Corrected to lowercase 'position'
        office: document.getElementById('teacherOffice').value,
    };
    if (method === 'PUT' && !teacherData.password) delete teacherData.password;

    const token = sessionStorage.getItem('token');
    const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
        body: JSON.stringify(teacherData)
    });

    if (response.ok) {
        alert(`Teacher ${id ? 'updated' : 'created'} successfully!`);
        closeModal('teacherModal');
        fetchAdminDashboard();
    } else {
        alert(`Failed to ${id ? 'update' : 'create'} teacher.`);
    }
}

async function handleStudentSubmit(event) {
    event.preventDefault();
    const id = document.getElementById('studentId').value;
    const url = id ? `/admin/student/${id}` : '/admin/student';
    const method = id ? 'PUT' : 'POST';

    const studentData = {
        rollNumber: document.getElementById('studentRollNumber').value,
        username: document.getElementById('studentUsername').value,
        password: document.getElementById('studentPassword').value,
        course: document.getElementById('studentCourse').value,
        batch: parseInt(document.getElementById('studentBatch').value),
        cgpa: parseFloat(document.getElementById('studentCgpa').value)
    };
    if (method === 'PUT' && !studentData.password) delete studentData.password;

    const token = sessionStorage.getItem('token');
    const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
        body: JSON.stringify(studentData)
    });

    if (response.ok) {
        alert(`Student ${id ? 'updated' : 'created'} successfully!`);
        closeModal('studentModal');
        fetchAdminDashboard();
    } else {
        alert(`Failed to ${id ? 'update' : 'create'} student.`);
    }
}

async function deleteUser(role, identifier) {
    if (!confirm(`Are you sure you want to delete this ${role}? This action cannot be undone.`)) return;
    const token = sessionStorage.getItem('token');
    const response = await fetch(`/admin/${role}/${identifier}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
    });
    if (response.ok) {
        alert(`${role} deleted successfully.`);
        fetchAdminDashboard();
    } else {
        alert(`Failed to delete ${role}.`);
    }
}

async function viewDetails(role, identifier) {
    const token = sessionStorage.getItem('token');
    const response = await fetch(`/admin/${role}/${identifier}`, { headers: { 'Authorization': `Bearer ${token}` }});
    if (!response.ok) { alert('Could not fetch details.'); return; }

    const data = await response.json();
    const detailsContent = document.getElementById('details-content');
    let contentHtml = '';

    if (role === 'teacher') {
        contentHtml = `
            <p><strong>ID:</strong> ${data.id}</p>
            <p><strong>Username:</strong> ${data.username}</p>
            <p><strong>Subject:</strong> ${data.subject}</p>
            <p><strong>Position:</strong> ${data.position}</p>
            <p><strong>Office:</strong> ${data.office || 'N/A'}</p>
            <p><strong>Assigned Students:</strong> ${data.studentRollNumbers.join(', ') || 'None'}</p>
        `;
    } else {
        contentHtml = `
            <p><strong>ID:</strong> ${data.id}</p>
            <p><strong>Roll Number:</strong> ${data.rollNumber}</p>
            <p><strong>Username:</strong> ${data.username}</p>
            <p><strong>Course:</strong> ${data.course}</p>
            <p><strong>Batch:</strong> ${data.batch}</p>
            <p><strong>CGPA:</strong> ${data.cgpa}</p>
            <p><strong>Marks:</strong> ${Object.keys(data.marks).length > 0 ? JSON.stringify(data.marks) : 'N/A'}</p>
            <p><strong>Notes:</strong> ${data.notes || 'N/A'}</p>
        `;
    }
    detailsContent.innerHTML = contentHtml;
    openModal('viewDetailsModal');
}

function openAssignStudentModal(teacherUsername) {
    document.getElementById('assign-teacher-name').innerText = teacherUsername;
    document.getElementById('assignTeacherUsername').value = teacherUsername;

    const selectList = document.getElementById('student-select-list');
    selectList.innerHTML = '<option value="">-- Select a Student --</option>';
    allStudents.forEach(student => {
        selectList.innerHTML += `<option value="${student.rollNumber}">${student.username} (${student.rollNumber})</option>`;
    });

    openModal('assignStudentModal');
}

async function assignStudent(event) {
    event.preventDefault();
    const token = sessionStorage.getItem('token');
    const teacherUsername = document.getElementById('assignTeacherUsername').value;
    const studentRollNumber = document.getElementById('student-select-list').value;
    if (!studentRollNumber) { alert('Please select a student.'); return; }

    const response = await fetch(`/admin/assign/${teacherUsername}/${studentRollNumber}`, {
        method: 'PUT',
        headers: { 'Authorization': `Bearer ${token}` }
    });

    if (response.ok) {
        alert('Student assigned successfully!');
        closeModal('assignStudentModal');
    } else {
        alert('Failed to assign student. They may already be assigned.');
    }
}


// --- STUDENT DASHBOARD ---

async function fetchStudentDashboard(rollNumber) {
    const token = sessionStorage.getItem('token');
    if (!token) { logout(); return; }

    const response = await fetch(`/sms/student/dashboard/${rollNumber}`, { headers: { 'Authorization': `Bearer ${token}` } });
    if (response.ok) {
        const data = await response.json();
        document.querySelector('.main-header h1').innerText = `Welcome, ${data.Profile.username}!`;
        const content = `
            <div class="content-card">
                <h2><i class="fas fa-user-circle"></i> My Profile</h2>
                <div class="info-grid">
                    <div class="info-item"><h3>Name</h3><p>${data.Profile.username}</p></div>
                    <div class="info-item"><h3>Roll Number</h3><p>${data.Profile.rollNumber}</p></div>
                    <div class="info-item"><h3>Course</h3><p>${data.Profile.course}</p></div>
                    <div class="info-item"><h3>Batch</h3><p>${data.Profile.batch}</p></div>
                    <div class="info-item"><h3>CGPA</h3><p>${data.Profile.cgpa}</p></div>
                </div>
            </div>
            <div class="content-card">
                 <h2><i class="fas fa-clipboard-list"></i> My Marks</h2>
                 <pre>${Object.keys(data.Marks).length > 0 ? JSON.stringify(data.Marks, null, 2) : 'No marks updated yet.'}</pre>
            </div>
             <div class="content-card">
                 <h2><i class="fas fa-sticky-note"></i> Notes from Teachers</h2>
                 <p>${data.Notes || 'No notes available.'}</p>
            </div>
        `;
        document.getElementById('dashboard-content').innerHTML = content;
    } else {
        document.getElementById('dashboard-content').innerHTML = '<div class="content-card"><h2>Access Denied or User Not Found</h2></div>';
    }
}

// --- TEACHER DASHBOARD ---

async function fetchTeacherDashboard(username) {
    const token = sessionStorage.getItem('token');
    if (!token) { logout(); return; }

    const response = await fetch(`/sms/teacher/dashboard/${username}`, { headers: { 'Authorization': `Bearer ${token}` } });
    if (response.ok) {
        const data = await response.json();
        document.querySelector('.main-header h1').innerText = `Welcome, ${data.Profile.username}!`;

        // Populate Profile Card
        const profileCard = document.getElementById('teacher-profile-card');
        profileCard.innerHTML = `
            <h2><i class="fas fa-user-circle"></i> My Profile</h2>
            <div class="info-grid">
                <div class="info-item"><h3>Name</h3><p>${data.Profile.username}</p></div>
                <div class="info-item"><h3>Subject</h3><p>${data.Profile.subject}</p></div>
                <div class="info-item"><h3>Position</h3><p>${data.Profile.position}</p></div>
                <div class="info-item"><h3>Office</h3><p>${data.Profile.office || 'N/A'}</p></div>
            </div>
        `;

        // Populate Assigned Students Table
        const studentsTbody = document.getElementById('assigned-students-tbody');
        studentsTbody.innerHTML = '';
        if (data.Students.length > 0) {
            data.Students.forEach(student => {
                studentsTbody.innerHTML += `<tr>
                    <td>${student.rollNumber}</td>
                    <td>${student.username}</td>
                    <td>${student.course}</td>
                    <td>${student.batch}</td>
                    <td>
                        <button class="action-btn edit-btn" onclick="openMarksModal('${student.rollNumber}', '${student.username}')" title="Update Marks"><i class="fas fa-check-double"></i> Marks</button>
                        <button class="action-btn assign-btn" onclick="openNotesModal('${student.rollNumber}', '${student.username}')" title="Update Notes"><i class="fas fa-sticky-note"></i> Notes</button>
                    </td>
                </tr>`;
            });
        } else {
            studentsTbody.innerHTML = '<tr><td colspan="5">No students have been assigned to you yet.</td></tr>';
        }

    } else {
        document.getElementById('dashboard-content').innerHTML = '<div class="content-card"><h2>Access Denied or User Not Found</h2></div>';
    }
}

function openMarksModal(rollNumber, studentName) {
    const teacherSubject = document.querySelector('#teacher-profile-card .info-item:nth-child(2) p').textContent;
    document.getElementById('marks-student-name').innerText = studentName;
    document.getElementById('marksStudentRollNumber').value = rollNumber;
    document.getElementById('teacher-subject-span').innerText = teacherSubject;
    openModal('marksModal');
}

function openNotesModal(rollNumber, studentName) {
    document.getElementById('notes-student-name').innerText = studentName;
    document.getElementById('notesStudentRollNumber').value = rollNumber;
    openModal('notesModal');
}

async function handleMarksSubmit(event) {
    event.preventDefault();
    const token = sessionStorage.getItem('token');
    const teacherUsername = sessionStorage.getItem('username');
    const rollNumber = document.getElementById('marksStudentRollNumber').value;
    const marks = document.getElementById('studentMarks').value;

    const params = new URLSearchParams();
    params.append('teacherUsername', teacherUsername);
    params.append('rollNumber', rollNumber);
    params.append('marks', marks);

    const response = await fetch('/sms/teacher/marks', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params
    });

    if (response.ok) {
        alert('Marks updated successfully!');
        closeModal('marksModal');
    } else {
        alert('Failed to update marks.');
    }
}

async function handleNotesSubmit(event) {
    event.preventDefault();
    const token = sessionStorage.getItem('token');
    const teacherUsername = sessionStorage.getItem('username');
    const rollNumber = document.getElementById('notesStudentRollNumber').value;
    const notes = document.getElementById('studentNotes').value;

    const params = new URLSearchParams();
    params.append('teacherUsername', teacherUsername);
    params.append('rollNumber', rollNumber);

    const response = await fetch('/sms/teacher/notes', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json' // Notes are sent in request body
        },
        body: JSON.stringify({ notes: notes })
    });

    if (response.ok) {
        alert('Notes updated successfully!');
        closeModal('notesModal');
    } else {
        alert('Failed to update notes.');
    }
}

