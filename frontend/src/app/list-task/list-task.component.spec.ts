import {
  TestBed,
  ComponentFixture,
  fakeAsync,
  tick,
} from '@angular/core/testing';
import { Router, ActivatedRoute } from '@angular/router';
import { TaskService } from '../service/task.service';
import { ListTaskComponent } from './list-task.component';
import { of, throwError } from 'rxjs';

describe('ListTaskComponent', () => {
  let component: ListTaskComponent;
  let fixture: ComponentFixture<ListTaskComponent>;
  let taskService: jest.Mocked<TaskService>;
  let router: jest.Mocked<Router>;
  let activatedRoute: ActivatedRoute;

  beforeEach(async () => {
    const taskServiceMock = {
      deleteTask: jest.fn(),
      getTasks: jest.fn(),
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
      imports: [],
      declarations: [ListTaskComponent],
      providers: [
        { provide: TaskService, useValue: taskServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ListTaskComponent);
    component = fixture.componentInstance;
    taskService = TestBed.inject(TaskService) as jest.Mocked<TaskService>;
    router = TestBed.inject(Router) as jest.Mocked<Router>;
    activatedRoute = TestBed.inject(ActivatedRoute);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch task list on initialization', () => {
    const mockResponse = {
      data: [
        {
          taskTitle: 'Task-1',
          taskDate: '2024-05-21',
          taskTime: '14:45',
          description: 'This is task 1',
        },
        {
          taskTitle: 'Task-2',
          taskDate: '2024-05-21',
          taskTime: '14:45',
          description: 'This is task 2',
        },
      ],
    };
    taskService.getTasks.mockReturnValue(of(mockResponse));

    component.ngOnInit();

    expect(taskService.getTasks).toHaveBeenCalled();
    expect(component.items).toEqual(mockResponse.data);
    expect(component.isError).toBe(false);
    expect(component.isLoading).toBe(false);
  });

  it('should handle error when fetching task list fails', () => {
    taskService.getTasks.mockReturnValue(
      throwError(() => ({ error: { data: 'Error message' } }))
    );

    component.ngOnInit();

    expect(taskService.getTasks).toHaveBeenCalled();
    expect(component.isError).toBe(true);
    expect(component.errorMessage).toBe('Error message');
    expect(component.isLoading).toBe(false);
  });

  it('should delete task successfully', fakeAsync(() => {
    const taskId = 123;
    taskService.deleteTask.mockReturnValue(of({}));
    taskService.getTasks.mockReturnValue(of({ data: [] }));

    component.deleteTask(taskId);
    tick();

    expect(taskService.deleteTask).toHaveBeenCalledWith(taskId);
    expect(taskService.getTasks).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['']);
    expect(component.isLoading).toBe(false);
  }));

  it('should handle delete task error', fakeAsync(() => {
    const taskId = 123;
    taskService.deleteTask.mockReturnValue(
      throwError(() => ({ error: { data: 'Delete error' } }))
    );

    component.deleteTask(taskId);
    tick();

    expect(component.isLoading).toBe(false);
    expect(component.isError).toBe(true);
    expect(component.errorMessage).toBe('Delete error');
  }));

  it('should navigate to update task route', () => {
    const taskId = 456;
    component.updateTask(taskId);
    expect(router.navigate).toHaveBeenCalledWith([`/update-task/${taskId}`]);
  });

  it('should close alerts', () => {
    component.isError = true;
    component.closeAlert();

    expect(component.isError).toBe(false);
  });

  it('should unsubscribe on destroy', () => {
    const spy = jest.spyOn(component.subscriptions, 'unsubscribe');
    component.ngOnDestroy();
    expect(spy).toHaveBeenCalled();
  });
});
