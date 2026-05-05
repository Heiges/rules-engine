import { Navigate } from 'react-router-dom'
import type { ReactNode } from 'react'
import { useRuleset } from '../context/RulesetContext'

interface RequireRoleProps {
  role: 'spielleiter' | 'spieler'
  children: ReactNode
}

export function RequireRole({ role, children }: RequireRoleProps) {
  const { role: currentRole } = useRuleset()
  if (currentRole !== role) return <Navigate to="/" replace />
  return <>{children}</>
}
