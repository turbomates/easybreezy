import { useCallback, useEffect } from "react"
import { useDispatch } from "react-redux"

export const useClearState = (clearStateAction: () => any) => {
  const dispatch = useDispatch()

  const clearState = useCallback(() => {
    dispatch(clearStateAction())
  }, [dispatch, clearStateAction])

  useEffect(() => {
    return () => clearState()
  }, [clearState])
}
