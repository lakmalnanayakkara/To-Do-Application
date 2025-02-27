import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick,
} from '@angular/core/testing';
import { AddTaskComponent } from './add-task.component';
import { TaskService } from '../service/task.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ReactiveFormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';

describe('AddTaskComponent', () => {
  let component: AddTaskComponent;
  let fixture: ComponentFixture<AddTaskComponent>;
  let taskService: jest.Mocked<TaskService>;
  let router: jest.Mocked<Router>;
  let activatedRoute: ActivatedRoute;

  beforeEach(async () => {
    const taskServiceMock = {
      addTask: jest.fn(),
      updateTask: jest.fn(),
      getTask: jest.fn(),
    };

    const routerMock = {
      navigate: jest.fn(),
    };

    const activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue(null),
        },
      },
    };

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [AddTaskComponent],
      providers: [
        { provide: TaskService, useValue: taskServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AddTaskComponent);
    component = fixture.componentInstance;
    taskService = TestBed.inject(TaskService) as jest.Mocked<TaskService>;
    router = TestBed.inject(Router) as jest.Mocked<Router>;
    activatedRoute = TestBed.inject(ActivatedRoute);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form for new task', () => {
    component.ngOnInit();
    expect(component.addTaskForm.pristine).toBe(true);
    expect(component.taskId).toBe(0);
  });

  it('should load existing task for editing', fakeAsync(() => {
    const mockTask = {
      data: {
        taskTitle: 'Test Task',
        taskDate: '2024-05-20',
        taskTime: '10:30',
        description: 'Test Description',
      },
    };

    activatedRoute.snapshot.paramMap.get = jest.fn().mockReturnValue('123');
    taskService.getTask.mockReturnValue(of(mockTask));

    component.ngOnInit();
    tick();

    expect(taskService.getTask).toHaveBeenCalledWith(123);
    expect(component.addTaskForm.value.title).toBe(mockTask.data.taskTitle);
    expect(component.isLoading).toBe(false);
  }));

  it('should handle error when loading task fails', fakeAsync(() => {
    activatedRoute.snapshot.paramMap.get = jest.fn().mockReturnValue('123');
    taskService.getTask.mockReturnValue(throwError(() => new Error('Error')));

    component.ngOnInit();
    tick();

    expect(component.isLoading).toBe(false);
    expect(component.isError).toBe(true);
  }));

  it('should submit valid form for new task', fakeAsync(() => {
    const mockResponse = {
      data: {
        taskTitle: 'New Task',
        taskDate: '2024-05-20',
        taskTime: '10:30',
        description: 'New Description',
      },
    };

    component.addTaskForm.setValue({
      title: 'New Task',
      taskDate: '2024-05-20',
      taskTime: new Date('2024-05-20T10:30'),
      description: 'New Description',
    });

    taskService.addTask.mockReturnValue(of(mockResponse));

    component.onSubmit();
    tick();

    expect(taskService.addTask).toHaveBeenCalled();
    expect(component.isSuccess).toBe(true);
    expect(component.message).toContain('added successfully');
    expect(component.addTaskForm.pristine).toBe(true);
  }));

  it('should update existing task', fakeAsync(() => {
    component.taskId = 123;
    const mockResponse = {
      data: {
        taskTitle: 'Updated Task',
        taskDate: '2024-05-21',
        taskTime: '14:45',
        description: 'Updated Description',
      },
    };

    component.addTaskForm.setValue({
      title: 'Updated Task',
      taskDate: '2024-05-21',
      taskTime: new Date('2024-05-21T14:45'),
      description: 'Updated Description',
    });

    taskService.updateTask.mockReturnValue(of(mockResponse));

    component.onSubmit();
    tick();

    expect(taskService.updateTask).toHaveBeenCalledWith(123, expect.anything());
    expect(router.navigate).toHaveBeenCalledWith(['']);
  }));

  it('should handle form submission error when add task', fakeAsync(() => {
    component.addTaskForm.setValue({
      title: 'New Task',
      taskDate: '2024-05-20',
      taskTime: new Date('2024-05-20T10:30'),
      description: 'New Description',
    });

    taskService.addTask.mockReturnValue(
      throwError(() => ({ error: { data: 'Error message' } }))
    );

    component.onSubmit();
    tick();

    expect(component.isError).toBe(true);
    expect(component.isLoading).toBe(false);
    expect(component.isSuccess).toBe(false);
    expect(component.message).toBe('Error message');
  }));

  it('should handle form submission error when update task', fakeAsync(() => {
    component.taskId = 123;
    component.addTaskForm.setValue({
      title: 'New Task',
      taskDate: '2024-05-20',
      taskTime: new Date('2024-05-20T10:30'),
      description: 'New Description',
    });

    taskService.updateTask.mockReturnValue(
      throwError(() => ({ error: { data: 'Error message' } }))
    );

    component.onSubmit();
    tick();

    expect(component.isError).toBe(true);
    expect(component.isLoading).toBe(false);
    expect(component.isSuccess).toBe(false);
    expect(component.message).toBe('Error message');
  }));

  it('should validate required fields', () => {
    component.addTaskForm.setValue({
      title: '',
      taskDate: '',
      taskTime: null,
      description: '',
    });

    expect(component.addTaskForm.invalid).toBe(true);
    expect(component.addTaskForm.controls.title.errors).toHaveProperty(
      'required'
    );
  });

  it('should format date correctly', () => {
    const date = new Date('2024-05-20');
    expect(component.formatDate(date)).toBe('2024-05-20');
  });

  it('should format time correctly', () => {
    const time = new Date('2024-05-20T14:30');
    expect(component.formatTime(time)).toBe('14:30');
  });

  it('should close alerts', () => {
    component.isError = true;
    component.isSuccess = true;
    component.closeAlert();

    expect(component.isError).toBe(false);
    expect(component.isSuccess).toBe(false);
  });

  it('should unsubscribe on destroy', () => {
    const spy = jest.spyOn(component.subscriptions, 'unsubscribe');
    component.ngOnDestroy();
    expect(spy).toHaveBeenCalled();
  });
});
